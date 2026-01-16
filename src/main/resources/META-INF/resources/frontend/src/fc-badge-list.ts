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
import { ResizeMixin } from '@vaadin/component-base/src/resize-mixin.js';
import '@vaadin/context-menu';
import type { ContextMenuItem } from '@vaadin/context-menu';
import badgeStylesContent from '../styles/badge.css?inline';
import { ThemableMixin } from '@vaadin/vaadin-themable-mixin/vaadin-themable-mixin.js';
import { css, html, LitElement, unsafeCSS } from 'lit';
import { customElement, property, query, queryAssignedNodes, state } from 'lit/decorators.js';

@customElement('fc-badge-list')
export class BadgeList extends ResizeMixin(ThemableMixin(LitElement)) {

  @query('[part~="overflow-badge"]')
  _overflowBadge!: HTMLDivElement
  
  @queryAssignedNodes({slot: 'badges', flatten: true})
  _badges!: Array<Node>;

  @query('[part~="container"]')
  _container!: HTMLDivElement

  @property()
  hiddenCount = 0;

  @property()
  theme : string | null = null;
  
  @property()
  label : ''
    
  @state()
  private overflowItems: ContextMenuItem[] = [];

  static styles = [
    unsafeCSS(badgeStylesContent),
    css`      
    
    :host {
      --badge-list-badges-margin: 0 calc(var(--lumo-space-s) / 2);
      --badge-list-label-color: var(--lumo-secondary-text-color);
      --badge-list-label-font-weight: 500;
      --badge-list-label-font-size: var(--lumo-font-size-s);
      --badge-list-label-margin-left: calc(var(--lumo-border-radius-m) / 4);
    }
    
    [part="container"] ::slotted(span[theme~="badge"]) {
	    margin: var(--badge-list-badges-margin);
    }

    [part="container"] ::slotted(span[theme~="badge"]:first-child) {
	    margin-left: 0;
    }
    
    [hidden] {
      display: none;
    }
    
    [part="container"] {
      position: relative;
      display: flex;
      width: 100%;
      flex-wrap: nowrap;
      overflow: hidden;
    }    
    
    [part="overflow-badge"] {
      margin: var(--badge-list-badges-margin);
    }   

    :host(:not([has-label])) [part='label']{
      display:none;
    }

    [part="label"] {
      align-self: flex-start;
      color: var(--badge-list-label-color);
      font-weight: var(--badge-list-label-font-weight);
      font-size: var(--badge-list-label-font-size);
      margin-left: var(--badge-list-label-margin-left);
      transition: color 0.2s;
      line-height: inherit;
      padding-right: 1em;
      padding-bottom: 0.5em;
      padding-top: 0.25em;
      margin-top: -0.25em;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      position: relative;
      max-width: 100%;
      box-sizing: border-box;
    }
    `
  ];

  static get is() {
    return 'fc-badge-list';
  }

  disconnectedCallback() {
    let parent = this.__parent;
    super.disconnectedCallback();
    parent.resizables = null;
  }
  
  _set_theme(theme : string) {
    this.theme = theme;
  }

  /**
   * Override getter from `ResizeMixin` to observe parent.
   *
   * @protected
   * @override
   */
  get _observeParent() {
    return true;
  }

  /**
   * Implement callback from `ResizeMixin` to update badges
   * and detect whether to show or hide the overflow badge.
   *
   * @protected
   * @override
   */
  _onResize() {
    this.__updateOnOverflow();
  }

  /**
   * Update the visible badges and the overflow badge count.
   * Also update the badges to be shown on overflow click.
   */
  __updateOnOverflow() {
    // get the container
    const container = this._container;
    // get the badges list
    let badges = this._badges;
    // get the overflow badge
    let overflow = this._overflowBadge;

    // Reset overflow badge for overflow calculations and get width
    overflow.removeAttribute("hidden");
    let overflowStyle = getComputedStyle(overflow);
    let overflowWidth = overflow.offsetWidth + parseInt(overflowStyle.marginInline);

    // Reset badges for calculations
    badges.forEach(badge => {
        badge.removeAttribute("hidden")
    });

    // verify which badges will be visible and which ones will be hidden
    let removeFromIndex = -1;
    let remainingWidth = container.offsetWidth - overflowWidth;

    for (let i = 0; i < badges.length; i++) {
      const element = badges[i];

      let elementStyle = getComputedStyle(element);
      let elementWidth = element.offsetWidth + parseInt(elementStyle.marginInlineStart) + parseInt(elementStyle.marginInlineEnd);
   
      if (elementWidth <= remainingWidth) {
        remainingWidth = remainingWidth - elementWidth;
      } else {
        removeFromIndex = i;
        break;
      }
    }

    // update badges visibility
    overflow.toggleAttribute('hidden', removeFromIndex < 0);
    if (removeFromIndex > -1) {
      for (let i = removeFromIndex; i < badges.length; i++) {
        const element = badges[i];
        element.setAttribute("hidden","");
      }
      this.hiddenCount = badges.length - removeFromIndex;
      this.__updateHiddenItems();
    } else {
      this.hiddenCount = 0;
      this.overflowItems = [];
    }
  }

  /**
   * Update overflow badge menu with the new hidden badges.
   */
  __updateHiddenItems() {
    const hiddenBadges = Array.from(this.querySelectorAll('[slot="badges"][hidden]'));
    this.overflowItems = [];
    hiddenBadges.forEach(hiddenBadge => {
      const copy = hiddenBadge.cloneNode(true);
      copy.removeAttribute("slot");
      copy.removeAttribute("hidden");
      copy.style.margin = '5px';
      const item = document.createElement('div');
      item.appendChild(copy);
      this.overflowItems.push({ component: item });
    });
  }

  render() {
    return html`
      <div part="label">
          <label for="container">${this.label}</label>
      </div>
      <div part="container" class="container" id="container">
   	    <slot name="badges"></slot>
        <vaadin-context-menu open-on="click" .items=${this.overflowItems}>
        	<span part="overflow-badge" theme="badge ${this.theme}" class="overflow-badge" hidden>
            <vaadin-icon icon="lumo:plus" style="padding: var(--lumo-space-xs)"></vaadin-icon>
            ${this.hiddenCount}
          </span>
      	</vaadin-context-menu>        
      </div>
    `;
  }
  
  update(_changedProperties: Map<PropertyKey, unknown>) {
    if (_changedProperties.has('label')) {
      this.toggleAttribute('has-label', this.label != null);
    }
    super.update(_changedProperties);
  }

}