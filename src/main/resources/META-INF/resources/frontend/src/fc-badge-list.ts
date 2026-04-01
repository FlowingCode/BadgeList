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
import '@vaadin/badge';
import { ThemableMixin } from '@vaadin/vaadin-themable-mixin/vaadin-themable-mixin.js';
import { ThemeDetectionMixin } from '@vaadin/vaadin-themable-mixin/vaadin-theme-detection-mixin.js';
import { css, html, LitElement } from 'lit';
import { customElement, property, query, queryAssignedNodes, state } from 'lit/decorators.js';
import { ifDefined } from 'lit/directives/if-defined.js';

@customElement('fc-badge-list')
export class BadgeList extends ResizeMixin(ThemableMixin(ThemeDetectionMixin(LitElement))) {

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

  @property({ attribute: 'data-application-theme' })
  _applicationTheme: string | null = null;

  private get _isAura(): boolean {
    return this._applicationTheme === 'aura';
  }

  static styles = [
    css`

    :host {
      --badge-list-badges-margin: 0 0.25rem;
      --badge-list-label-color: currentColor;
      --badge-list-label-font-weight: 500;
      --badge-list-label-font-size: 0.875rem;
      --badge-list-label-margin-left: 0;
    }

    :host([data-application-theme="lumo"]) {
      --badge-list-badges-margin: 0 calc(var(--lumo-space-s) / 2);
      --badge-list-label-color: var(--lumo-secondary-text-color);
      --badge-list-label-font-size: var(--lumo-font-size-s);
      --badge-list-label-margin-left: calc(var(--lumo-border-radius-m) / 4);
    }

    vaadin-context-menu {
      line-height: 0;
    }

    [part="container"] ::slotted(vaadin-badge) {
	    margin: var(--badge-list-badges-margin);
    }

    [part="container"] ::slotted(vaadin-badge:first-child) {
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

    :host([data-application-theme="aura"]) [part="overflow-badge"] {
      color: var(--vaadin-badge-text-color, var(--aura-accent-text-color));
      background: var(--vaadin-badge-background, var(--aura-accent-surface) padding-box);
      border-color: var(--vaadin-badge-border-color, var(--aura-accent-border-color));
      font-size: var(--vaadin-badge-font-size, var(--aura-font-size-s));
      --aura-surface-level: 1;
    }

    :host([data-application-theme="aura"]) [part="overflow-badge"]:is([theme~='filled'], [theme~='dot']) {
      background: var(--aura-accent-color);
      color: var(--aura-accent-contrast-color);
    }

    [part="overflow-badge"] vaadin-icon {
      width: 0.75em;
      height: 0.75em;
    }

    :host([data-application-theme="lumo"]) [part="overflow-badge"] vaadin-icon {
      width: 1em;
      height: 1em;
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
      const copy = hiddenBadge.cloneNode(true) as HTMLElement;
      copy.removeAttribute("slot");
      copy.removeAttribute("hidden");
      copy.style.margin = '5px';
      // Copy computed host styles so clones render correctly inside the overlay.
      if (this._isAura) {
        const computed = getComputedStyle(hiddenBadge as HTMLElement);
        copy.style.color = computed.color;
        copy.style.backgroundColor = computed.backgroundColor;
        copy.style.backgroundClip = computed.backgroundClip;
        copy.style.borderTopColor = computed.borderTopColor;
        copy.style.borderRightColor = computed.borderRightColor;
        copy.style.borderBottomColor = computed.borderBottomColor;
        copy.style.borderLeftColor = computed.borderLeftColor;
        copy.style.fontSize = computed.fontSize;
      }
      const item = document.createElement('div');
      item.appendChild(copy);
      this.overflowItems.push({ component: item });
    });
  }

  render() {
    const icon = this._isAura ? 'vaadin:plus' : 'lumo:plus';
    return html`
      <div part="label">
          <label for="container">${this.label}</label>
      </div>
      <div part="container" class="container" id="container">
   	    <slot name="badges"></slot>
        <vaadin-context-menu open-on="click" .items=${this.overflowItems}>
          <vaadin-badge part="overflow-badge" theme="${ifDefined(this.theme || undefined)}" hidden>
            <vaadin-icon icon="${icon}" slot="icon"></vaadin-icon>
            ${this.hiddenCount}
          </vaadin-badge>
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