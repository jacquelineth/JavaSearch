/*
 * JavaTB.
 * Copyright (C) 2008-2014 JavaTB Team.
 * http://javatb.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	 http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.javatb.search.ui.results.actions;

import org.javatb.search.SearchElement;
import org.javatb.search.ui.results.ResultRenderer;
import org.javatb.util.FileUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Action for opening the selected result location using their OS associated applications.
 * @author Thierry Jacqueline
 */
public class ExploreFileLocationAction extends AbstractResultAction{
    /**
     * Initialize this action.
     */
    public ExploreFileLocationAction(final ResultRenderer renderer)
    {
        super(renderer);
        putValue(Action.NAME, "Explore");
    }
    /**
     * Perform the "Explore" action on each selected element.
     * @param event not used.
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed( final ActionEvent event) {
        List<SearchElement> sel = getSelection();
        for (SearchElement elt: sel)
        {
            if (elt.isFile() || elt.isArchiveEntry())
            {
                FileUtils.exploreOnSearchElement(elt);
            }
        }
    }
}
