/*-
 * #%L
 * Badge List Add-on
 * %%
 * Copyright (C) 2023 - 2026 Flowing Code
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

import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.flowingcode.vaadin.addons.demo.SourceCodeViewer;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.List;

@DemoSource
//#if vaadin eq 0
@DemoSource(value = "/src/test/resources/META-INF/frontend/styles/styled-badges-demo.css",
    caption = "styled-badges-demo.css")
@DemoSource(value = "/src/test/resources/META-INF/frontend/styles/styled-badges-demo-v25.css",
    caption = "styled-badges-demo-v25.css")
@DemoSource(value = "/src/test/resources/META-INF/frontend/styles/fc-badge-list.css",
    caption = "fc-badge-list.css")
//#endif
@PageTitle("Styled Badges Demo")
@SuppressWarnings("serial")
//#if vaadin eq 24
@CssImport("./styles/styled-badges-demo.css")
//#else
@CssImport(value = "./styles/fc-badge-list.css", themeFor = "fc-badge-list")
@CssImport("./styles/styled-badges-demo-v25.css")
//#endif
@Route(value = "badge-list/styled", layout = BadgeListDemoView.class)
public class StyledBadgesDemo extends BaseBadgeListDemo {

  public StyledBadgesDemo() {
    // begin-block example1 
    List<Badge> badges1 = new ArrayList<>();
    for (int i = 0; i < 8; i++) {
      Badge badge = new Badge("BADGE" + (i + 1));
      badge.addThemeName("error primary");
      badges1.add(badge);
    }
    BadgeList badgeList1 = new BadgeList(badges1);
    Div layout1 = new Div(badgeList1);
    layout1.setWidth("350px");
    // #if vaadin eq 0
    Div example1 = this.createContainerDiv("Badges with 'Error' and 'Primary' variants", layout1);
    SourceCodeViewer.highlightOnHover(example1, "example1");
    add(example1);
    this.addSeparator();
    // #endif
    // show-source add(layout1);
    // end-block

    // begin-block example2
    List<Badge> badges2 = new ArrayList<>();
    for (int i = 0; i < 12; i++) {
      Badge badge = new Badge("BADGE" + (i + 1));
      badge.addClassName("custom-styled-badge");
      badges2.add(badge);
    }
    BadgeList badgeList2 = new BadgeList(badges2);
    badgeList2.addClassName("styled-badges-second-example");
    Div layout2 = new Div(badgeList2);
    layout2.setWidth("450px");
    // #if vaadin eq 0
    Div example2 = this.createContainerDiv("Badges with custom styling", layout2);
    SourceCodeViewer.highlightOnHover(example2, "example2");
    add(example2);
    this.addSeparator();
    // #endif
    // show-source add(layout2);
    // end-block

    // begin-block example3
    List<Badge> badges3 = new ArrayList<>();
    for (int i = 0; i < 12; i++) {
      Badge badge = new Badge(createIcon(VaadinIcon.CHECK), new Span("BADGE" + (i + 1)));
      badge.addThemeName("success");
      badges3.add(badge);
    }
    BadgeList badgeList3 = new BadgeList(badges3);
    badgeList3.addThemeName("success");
    Div layout3 = new Div(badgeList3);
    // #if vaadin eq 0
    Div example3 = this.createContainerDiv(
        "Badges & Overflow with 'Success' variant and badges with icon", layout3);
    SourceCodeViewer.highlightOnHover(example3, "example3");
    add(example3);
    // #endif
    // show-source add(layout3);
    // end-block
  }

  // #if vaadin eq 0
  private Icon createIcon(VaadinIcon vaadinIcon) {
    Icon icon = vaadinIcon.create();
    icon.getStyle().set("padding", "var(--lumo-space-xs");
    return icon;
  }
  // #endif
}
