/*
 * $Id$
 *
 * Copyright (c) 2012-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.simsilica.lemur;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An object of class <code>CheckboxGroup</code> is used to create a
 * set of checkboxes where only a single component can be activated.
 * <p>
 * This class is similar to the {@code javax.swing.ButtonGroup} class of
 * Java desktop, uses the same logic.</p>
 * 
 * @author wil
 * @since 1.16.1-SNAPSHOT
 */
@SuppressWarnings(value = {"serial"})
public class CheckboxGroup implements Serializable {    
    /** Serial ID. */
    static final long serialVersionUID = 10L;
    
    /**
     * The list of checkboxs participating in this group.
     */
    protected List<Checkbox> checkboxs = new ArrayList<>();
    
    /**
     * The current selection.
     */
    CheckboxModel selection = null;

    /**
     * Creates a new <code>CheckboxGroup</code>.
     */
    public CheckboxGroup() {        
    }
    
    /**
     * Adds the checkbox to the group.
     * @param c the checkbox to be added
     */
    public void add(Checkbox c) {
        if(c == null) {
            return;
        }
        checkboxs.add(c);

        if (c.isChecked()) {
            if (selection == null) {
                selection = c.getModel();
            } else {
                c.setChecked(false);
            }
        }

        c.getModel().setGroup(this);
    }
    
    /**
     * Removes the checkbox from the group.
     * @param c the checkbox to be removed
     */
    public void remove(Checkbox c) {
        if(c == null) {
            return;
        }
        checkboxs.remove(c);
        if(c.getModel() == selection) {
            selection = null;
        }
        c.getModel().setGroup(null);
    }
    
    /**
     * Clears the selection such that none of the checkboxs
     * in the <code>CheckboxGroup</code> are selected.
     */
    public void clearSelection() {
        if (selection != null) {
            CheckboxModel oldSelection = selection;
            selection = null;
            oldSelection.setChecked(false);
        }
    }
    
    /**
     * Returns all the buttons that are participating in
     * this group.
     * @return an <code>Iterator</code> of the checkboxs in this group
     */
    public Iterator<Checkbox> getElements() {
        return checkboxs.iterator();
    }
    
    /**
     * Returns the model of the selected checkbox.
     * @return the selected checkbox model
     */
    public CheckboxModel getSelection() {
        return selection;
    }
    
    /**
     * Sets the selected value for the <code>CheckboxModel</code>.
     * Only one checkbox in the group may be selected at a time.
     * @param m the <code>CheckboxModel</code>
     * @param b <code>true</code> if this checkbox is to be
     *   selected, otherwise <code>false</code>
     */
    public void setSelected(CheckboxModel m, boolean b) {
        if (b && m != null && m != selection) {
            CheckboxModel oldSelection = selection;
            selection = m;
            if (oldSelection != null) {
                oldSelection.setChecked(false);
            }
            m.setChecked(true);
        }
    }
    
    /**
     * Returns whether a {@code CheckboxModel} is selected.
     *
     * @param m an isntance of {@code CheckboxModel}
     * @return {@code true} if the checkbox is selected,
     *   otherwise returns {@code false}
     */
    public boolean isSelected(CheckboxModel m) {
        return (m == selection);
    }

    /**
     * Returns the number of checkboxs in the group.
     * @return the checkbox count
     */
    public int getCheckboxCount() {
        if (checkboxs == null) {
            return 0;
        } else {
            return checkboxs.size();
        }
    }
}
