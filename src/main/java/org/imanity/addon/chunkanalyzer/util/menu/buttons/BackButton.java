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

package org.imanity.addon.chunkanalyzer.util.menu.buttons;

import org.bukkit.ChatColor;
import org.imanity.addon.chunkanalyzer.util.item.ItemBuilder;
import org.imanity.addon.chunkanalyzer.util.menu.Button;
import org.imanity.addon.chunkanalyzer.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BackButton extends Button {

	private final Menu back;

	public BackButton(Menu back) {
		this.back = back;
	}

	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(Material.STAINED_GLASS_PANE).data(3).name(ChatColor.RED + "Back").build();
	}

	public void clicked(Player player, int i, ClickType clickType, int hb) {
		playNeutral(player);
		this.back.openMenu(player);
	}
}
