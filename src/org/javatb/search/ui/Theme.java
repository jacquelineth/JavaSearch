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
package org.javatb.search.ui;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.*;

/**
 * Color theme for the UI.
 * @author Laurent Cohen
 */
public class Theme
{
  private static AtomicInteger count = new AtomicInteger(0);
	public static final Theme DEFAULT = new Theme("System Default", SystemColor.control, SystemColor.controlText, SystemColor.window, SystemColor.windowText);
	public static final Theme ORANGE = new Theme("Orange", Color.yellow, Color.black, Color.orange, Color.black);
	public static final Theme BLUE = new Theme("Blue", new Color(0, 0, 96), Color.white, new Color(92, 128, 228), Color.white);
	public static final Theme GREEN = new Theme("Green", new Color(38, 141, 33), Color.white, new Color(167, 195, 123), Color.white);
	public static final Theme GIRLIE = GREEN.reverseBackground().setName("Girlie");
	public static final Theme DESERT = BLUE.reverse().setName("Desert");
	public static final Theme BARBIE = new Theme("Barbie", new Color(128, 128, 255), Color.white, new Color(255, 128, 192), Color.white);
	public static final Theme TEST = new Theme("Test", new Color(167, 195, 123), Color.white, new Color(38, 141, 33), Color.white);

	private Color containerBackground = SystemColor.window;
	private Color containerForeground = SystemColor.windowText;
	private Color componentBackground = SystemColor.control;
	private Color componentForeground = SystemColor.controlText;
	private String name = null;

	private Theme(Color c1, Color c2, Color c3, Color c4)
	{
		this("No Name "+count.incrementAndGet(), c1, c2, c3, c4);
	}

	private Theme(String name, Color c1, Color c2, Color c3, Color c4)
	{
		this.name = name;
		containerBackground = c1;
		containerForeground = c2;
		componentBackground = c3;
		componentForeground = c4;
	}

	public Color getContainerBackground()
	{
		return containerBackground;
	}

	public Color getContainerForeground()
	{
		return containerForeground;
	}

	public Color getComponentBackground()
	{
		return componentBackground;
	}

	public Color getComponentForeground()
	{
		return componentForeground;
	}

	public void setColors(JComponent comp)
	{
		if (comp instanceof JPanel)
		{
			comp.setBackground(getContainerBackground());
			comp.setForeground(getContainerForeground());
		}
		else
		{
			comp.setBackground(getComponentBackground());
			comp.setForeground(getComponentForeground());
		}
	}

	public Theme reverse()
	{
		return new Theme(reverseColor(containerBackground), reverseColor(containerForeground),
			reverseColor(componentBackground), reverseColor(componentForeground));
	}

	public Theme reverseBackground()
	{
		return new Theme(reverseColor(containerBackground), containerForeground,
			reverseColor(componentBackground), componentForeground);
	}

	private Color reverseColor(Color c)
	{
		int r = 255 - c.getRed();
		int g = 255 - c.getGreen();
		int b = 255 - c.getBlue();
		return new Color(r, g, b);
	}

	public Theme swap()
	{
		return new Theme(componentBackground, componentForeground, containerBackground, containerForeground);
	}

	public Theme setName(String name)
	{
		this.name = name;
		return this;
	}
}
