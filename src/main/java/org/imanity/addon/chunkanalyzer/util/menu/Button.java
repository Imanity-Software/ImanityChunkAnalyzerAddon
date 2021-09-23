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

package org.imanity.addon.chunkanalyzer.util.menu;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Button {

	public static org.imanity.addon.chunkanalyzer.util.menu.Button placeholder(final Material material, final byte data, String title) {
		return (new org.imanity.addon.chunkanalyzer.util.menu.Button() {
			public ItemStack getButtonItem(Player player) {
				ItemStack it = new ItemStack(material, 1, data);
				ItemMeta meta = it.getItemMeta();

				meta.setDisplayName(title);
				it.setItemMeta(meta);

				return it;
			}
		});
	}

	public static void playFail(Player player) {
		player.playSound(player.getLocation(), Sound.DIG_GRASS, 20F, 0.1F);
	}

	public static void playSuccess(Player player) {
		player.playSound(player.getLocation(), Sound.NOTE_PIANO, 20F, 15F);
	}

	public static void playNeutral(Player player) {
		player.playSound(player.getLocation(), Sound.CLICK, 20F, 1F);
	}

	public abstract ItemStack getButtonItem(Player player);

	public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
	}

	public boolean shouldCancel(Player player, int slot, ClickType clickType) {
		return (true);
	}

	public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
		return (false);
	}

}
