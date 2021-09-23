/*
 * MIT License
 *
 * Copyright (c) 2021 Imanity Software
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.imanity.addon.chunkanalyzer.util.menu.pagination;

import org.imanity.addon.chunkanalyzer.util.item.ItemBuilder;
import org.imanity.addon.chunkanalyzer.util.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class JumpToPageButton extends Button {

	private final int page;
	private final PaginatedMenu menu;
	private final boolean current;

	@Override
	public ItemStack getButtonItem(Player player) {
		ItemBuilder builder = new ItemBuilder(this.current ? Material.ENCHANTED_BOOK : Material.BOOK)
				.amount(this.page)
				.name("§b" + this.page
						+ (this.page == 1 ? "st"
						: this.page == 2 ? "nd"
						: this.page == 3 ? "rd" : "th")
						+ " page" + (this.current ? " §7(Current page)" : ""));
		return builder.build();
	}

	@Override
	public void clicked(Player player, int i, ClickType clickType, int hb) {
		this.menu.modPage(player, this.page - this.menu.getPage());
		playNeutral(player);
	}
}
