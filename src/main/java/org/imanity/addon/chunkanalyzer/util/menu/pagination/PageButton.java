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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class PageButton extends Button {

	private final int mod;
	private final PaginatedMenu menu;

	@Override
	public ItemStack getButtonItem(Player player) {
		ItemBuilder builder = new ItemBuilder(Material.CARPET);

		if (hasNext(player)) {
			builder.name(this.mod > 0 ? ChatColor.GREEN +
					"Next page"
					: ChatColor.RED + "Previous page")
					.lore(
							"§7§m*------------------------------*",
							"§f§l» §bLeft click",
							" §7§l* §fTo go to this page",
							"§f§l» §bRight click",
							" §7§l* §fTo select the page you want",
							"&7&m*------------------------------*");
		} else {
			builder.name(ChatColor.AQUA + (this.mod > 0 ?
					"Last page" :
					"First page"))
					.lore(
							"§7§m*------------------------------*",
							"§f§l» §bRight click",
							" §7§l* §fTo select the page you want",
							"§7§m*------------------------------*");
		}
		return builder.build();
	}

	@Override
	public void clicked(Player player, int i, ClickType clickType, int hb) {
		if (clickType == ClickType.RIGHT) {
			new ViewAllPagesMenu(this.menu).openMenu(player);
			playNeutral(player);
		} else {
			if (hasNext(player)) {
				this.menu.modPage(player, this.mod);
				playNeutral(player);
			} else {
				playFail(player);
			}
		}
	}

	private boolean hasNext(Player player) {
		int pg = this.menu.getPage() + this.mod;
		return pg > 0 && this.menu.getPages(player) >= pg;
	}
}
