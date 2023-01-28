/*
 * $Id$
 *
 * Copyright (c) 2015, Simsilica, LLC
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.simsilica.lemur.math;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.math.ColorRGBA;

import java.io.IOException;
import java.io.Serializable;

/**
 * <code>ColorHSBA</code> is responsible for encapsulating the color data
 * of a <code>ColorChooser</code> component.
 * <p>
 * With this object we can convert from <b>RGBA</b> which is the format of
 * color that is used in jme for <code>HSBA</code> that is the format that
 * handles the <code>ColorChooser</code> component via the
 * following methods:
 * <pre><code>
 * toJmeColorHSBA(com.jme3.math.ColorRGBA)            // from rgba to hsb
 * toJmeColorRGBA(com.simsilica.lemur.math.ColorHSBA) // from hsb to rgba
 * </code></pre>.
 * 
 * @author wil 
 * @since 1.16.1-SNAPSHOT
 */
public final 
class ColorHSBA implements Cloneable, Savable, Serializable {
    
    private float h;
    private float s;
    private float b;
    private float a;
    
    public ColorHSBA() {
        this(0.0F, 0.0F, 0.0F, 0.5F);
    }
    
    public ColorHSBA(ColorHSBA c) {
        this(c.h, c.s, c.b, c.a);
    }

    public ColorHSBA(float h, float s, float b, float a) {
        this.h = h;
        this.s = s;
        this.b = b;
        this.a = a;
    }

    @Override
    public ColorHSBA clone() {
        try {
            ColorHSBA clon = (ColorHSBA) super.clone();
            clon.a = a; clon.b = b;
            clon.h = h; clon.s = s;
            return clon;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    
    /*
        Setters.
    */
    public void setHue(float h)   { this.h = h; }
    public void setAlpha(float a) { this.a = a; }
    public void setSaturation(float s) { this.s = s; }
    public void setBrightness(float b) { this.b = b; }
    
    /*
        Getters.
    */
    public float getHue()   { return h; }
    public float getAlpha() { return a; }
    public float getSaturation() { return s; }
    public float getBrightness() { return b; }

    @Override
    public String toString() {
        return "ColorHSBA [" + "h=" + h + ", s=" + s + ", b=" + b + ", a=" + a + ']';
    }
    
    public static ColorHSBA toJmeColorHSBA(ColorRGBA rgba) {
        if (rgba == null)
            throw new NullPointerException("ColorRGBA is null.");
        
        final float[] hsbavals= toHSB(rgba.r, rgba.g, rgba.b, rgba.a);
        return new ColorHSBA(hsbavals[0], hsbavals[1], hsbavals[2], hsbavals[3]);
    }
    
    private static float[] toHSB(float r, float g, float b, float a) {
        float hue, saturation, brightness;
        float hsbvals[] = new float[4];
            
        float cmax = (r > g) ? r : g;
        if (b > cmax) cmax = b;
        float cmin = (r < g) ? r : g;
        if (b < cmin) cmin = b;

        brightness = ((float) cmax) / 1.0f;
        if (cmax != 0)
            saturation = ((float) (cmax - cmin)) / ((float) cmax);
        else
            saturation = 0;
        if (saturation == 0)
            hue = 0;
        else {
            float redc = ((float) (cmax - r)) / ((float) (cmax - cmin));
            float greenc = ((float) (cmax - g)) / ((float) (cmax - cmin));
            float bluec = ((float) (cmax - b)) / ((float) (cmax - cmin));
            if (r == cmax)
                hue = bluec - greenc;
            else if (g == cmax)
                hue = 2.0f + redc - bluec;
            else
                hue = 4.0f + greenc - redc;
            hue = hue / 6.0f;
            if (hue < 0)
                hue = hue + 1.0f;
        }
        hsbvals[0] = hue;
        hsbvals[1] = saturation;
        hsbvals[2] = brightness;
        hsbvals[3] = a;
        return hsbvals;
    }
    
    public static ColorRGBA toJmeColorRGBA(ColorHSBA hsba) {
        if (hsba == null)
            throw new NullPointerException("ColorHSBA is null.");
        return toRGBA(hsba.h, hsba.s, hsba.b, hsba.a);
    }
    
    private static ColorRGBA toRGBA(float hue, float saturation, float brightness, float alpha) {
        float r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = brightness;
        } else {
            float h = (hue - (float) Math.floor(hue)) * 6.0f;
            float f = h - (float) java.lang.Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
                case 0:
                    r = brightness;
                    g = t;
                    b = p;
                    break;
                case 1:
                    r = q;
                    g = brightness;
                    b = p;
                    break;
                case 2:
                    r = p;
                    g = brightness;
                    b = t;
                    break;
                case 3:
                    r = p;
                    g = q;
                    b = brightness;
                    break;
                case 4:
                    r = t;
                    g = p;
                    b = brightness;
                    break;
                case 5:
                    r = brightness;
                    g = p;
                    b = q;
                    break;
                 default:
                     throw new AssertionError();
            }
        }
        return new ColorRGBA(r, g, b, alpha);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule out = ex.getCapsule(this);        
        out.write(h, "hue", 0f);
        out.write(s, "saturation", 0f);
        out.write(b, "brightness", 0.5f);
        out.write(a, "alpha", 1.0f);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule in = im.getCapsule(this);        
        h = in.readFloat("hue", 0f);
        s = in.readFloat("saturation", 0f);
        b = in.readFloat("brightness", 0.5f);
        a = in.readFloat("alpha", 1.0f);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Float.floatToIntBits(this.h);
        hash = 79 * hash + Float.floatToIntBits(this.s);
        hash = 79 * hash + Float.floatToIntBits(this.b);
        hash = 79 * hash + Float.floatToIntBits(this.a);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ColorHSBA other = (ColorHSBA) obj;
        if (Float.floatToIntBits(this.h) != Float.floatToIntBits(other.h)) {
            return false;
        }
        if (Float.floatToIntBits(this.s) != Float.floatToIntBits(other.s)) {
            return false;
        }
        if (Float.floatToIntBits(this.b) != Float.floatToIntBits(other.b)) {
            return false;
        }
        return Float.floatToIntBits(this.a) == Float.floatToIntBits(other.a);
    }
}
