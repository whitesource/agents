/**
 * Copyright (C) 2014 WhiteSource Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.whitesource.agent.api.model;

import java.io.Serializable;

/**
 * Holds the copyright information found in a file.
 *
 * @author tom.shapira
 */
public class CopyrightInfo implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = 7366184386342223073L;

    /* --- Members --- */

    private String copyright;

    private int line;

    /* --- Constructors --- */

    /**
     * Default constructor.
     */
    public CopyrightInfo() {
    }

    /**
     * Constructor.
     *
     * @param copyright The actual line of code containing the copyright information.
     * @param line The code line number.
     */
    public CopyrightInfo(String copyright, int line) {
        this.copyright = copyright;
        this.line = line;
    }

    /* --- Getters / Setters --- */

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }
}
