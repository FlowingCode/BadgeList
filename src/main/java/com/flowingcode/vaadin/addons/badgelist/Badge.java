/*-
 * #%L
 * Badge List Add-on
 * %%
 * Copyright (C) 2023 - 2024 Flowing Code
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.flowingcode.vaadin.addons.badgelist;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;

/** Component representing a badge object. */
public class Badge extends Span {

  /** Creates a new empty badge. */
  public Badge() {
    super();
    this.addThemeName("badge");
  }

  /**
   * Creates a new badge with the given text.
   *
   * @param text the badge's text
   */
  public Badge(String text) {
    this();
    setText(text);
  }

  /**
   * Creates a new badge with the given child components.
   *
   * @param components the badge's components
   */
  public Badge(Component... components) {
    this();
    add(components);
  }

  /**
   * Adds a theme name to this component.
   *
   * @param themeName the theme name to add
   */
  public void addThemeName(String themeName) {
    this.getElement().getThemeList().add(themeName);
  }

  /**
   * Removes a theme name from this component.
   *
   * @param themeName the theme name to remove
   */
  public void removeThemeName(String themeName) {
    this.getElement().getThemeList().remove(themeName);
  }
}
