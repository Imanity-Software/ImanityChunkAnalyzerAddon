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

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.imanity.addon.chunkanalyzer.util.menu.task.MenuUpdateTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public abstract class Menu {

	private static boolean initialized;

	public static void init(JavaPlugin plugin) {
		if (initialized) {
			throw new IllegalStateException("Menu system already initialized!");
		}
		plugin.getServer().getScheduler().runTaskTimer(plugin, new MenuUpdateTask(), 100L, 100L);
		plugin.getServer().getPluginManager().registerEvents(new ButtonListener(plugin), plugin);

		initialized = true;
	}

	public static final Map<UUID, Menu> MENUS = new ConcurrentHashMap<>();

	@Getter
	private Map<Integer, org.imanity.addon.chunkanalyzer.util.menu.Button> buttons = new HashMap<>();
	private boolean autoUpdate = true;
	private boolean updateAfterClick = true;
	private boolean autoClose = false;
	private boolean closedByMenu = false;

	@Setter
	private boolean placeholder = false;

	@Setter
	private boolean fillBorders = false;

	private long openMillis, lastAccessMillis;
	private org.imanity.addon.chunkanalyzer.util.menu.Button placeholderButton = org.imanity.addon.chunkanalyzer.util.menu.Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 15, " ");

	private ItemStack createItemStack(final Player player, final org.imanity.addon.chunkanalyzer.util.menu.Button button) {
		return button.getButtonItem(player);
	}

	public void openMenu(final Player player) {
		this.openMenu(player, false);
		openMillis = System.currentTimeMillis();
		lastAccessMillis = openMillis;
	}

	public void openMenu(final Player player, boolean update) {
		this.buttons = this.getButtons(player);

		final org.imanity.addon.chunkanalyzer.util.menu.Menu previousMenu = org.imanity.addon.chunkanalyzer.util.menu.Menu.MENUS.get(player.getUniqueId());
		Inventory inventory = null;
		final int size = this.getSize() == -1 ? this.size(this.buttons) : this.getSize();
		String title = this.getTitle(player).replace("&", "§");

		if (title.length() > 32) {
			title = title.substring(0, 32);
		}

		if (player.getOpenInventory() != null) {
			if (previousMenu == null) {
				player.closeInventory();
			} else {
				final int previousSize = player.getOpenInventory().getTopInventory().getSize();

				if (previousSize == size && player.getOpenInventory().getTopInventory().getTitle().equals(title)) {
					inventory = player.getOpenInventory().getTopInventory();
					update = true;
				} else {
					previousMenu.setClosedByMenu(true);
					player.closeInventory();
				}
			}
		}

		if (inventory == null) {
			inventory = Bukkit.createInventory(player, size, title);
		}

		inventory.setContents(new ItemStack[inventory.getSize()]);

		MENUS.put(player.getUniqueId(), this);

		for (final Map.Entry<Integer, org.imanity.addon.chunkanalyzer.util.menu.Button> buttonEntry : this.buttons.entrySet()) {
			inventory.setItem(buttonEntry.getKey(), createItemStack(player, buttonEntry.getValue()));
		}

		if (this.isPlaceholder()) {
			for (int index = 0; index < size; index++) {
				if (this.buttons.get(index) == null) {
					this.buttons.put(index, this.placeholderButton);
					inventory.setItem(index, this.placeholderButton.getButtonItem(player));
				}
			}
		} else if (this.isFillBorders() && size >= 27) { // Requires 3 rows of inventory to do it
			for (int index = 0; index < 9; index++) {
				if (this.buttons.get(index) == null) {
					this.buttons.put(index, this.placeholderButton);
					inventory.setItem(index, this.placeholderButton.getButtonItem(player));
				}
			}
			for (int index = 9; index < size - 9; index+= 9) {
				if (this.buttons.get(index) == null) {
					this.buttons.put(index, this.placeholderButton);
					inventory.setItem(index, this.placeholderButton.getButtonItem(player));
				}

				if (this.buttons.get(index + 8) == null) {
					this.buttons.put(index + 8, this.placeholderButton);
					inventory.setItem(index + 8, this.placeholderButton.getButtonItem(player));
				}
			}
			for (int index = size - 9; index < size; index++) {
				if (this.buttons.get(index) == null) {
					this.buttons.put(index, this.placeholderButton);
					inventory.setItem(index, this.placeholderButton.getButtonItem(player));
				}
			}
		}

		if (update) {
			player.updateInventory();
		} else {
			player.openInventory(inventory);
		}

		this.onOpen(player);
		this.setClosedByMenu(false);
	}

	public Map<Integer, Button> getButtonsUpdatable(Player player) {
		return this.getButtons(player);
	}

	public void update(Player player, boolean removingOld) {
		this.buttons = this.getButtonsUpdatable(player);
		int size = this.getSize() == -1 ? this.size(this.buttons) : this.getSize();
		Inventory inventory = player.getOpenInventory().getTopInventory();
		if (inventory != null) {
			if (removingOld) {
				inventory.setContents(new ItemStack[inventory.getSize()]);
			}

			for (Map.Entry<Integer, Button> integerButtonEntry : this.buttons.entrySet()) {
				inventory.setItem(integerButtonEntry.getKey(), this.createItemStack(player, integerButtonEntry.getValue()));
			}

			if (this.isPlaceholder()) {
				for(int index = 0; index < size; ++index) {
					if (this.buttons.get(index) == null) {
						this.buttons.put(index, this.placeholderButton);
						inventory.setItem(index, this.placeholderButton.getButtonItem(player));
					}
				}
			}

			player.updateInventory();
		}
	}

	public int size(final Map<Integer, org.imanity.addon.chunkanalyzer.util.menu.Button> buttons) {
		int highest = 0;

		for (final int buttonValue : buttons.keySet()) {
			if (buttonValue > highest) {
				highest = buttonValue;
			}
		}

		return (int) (Math.ceil((highest + 1) / 9D) * 9D);
	}

	public int getSlot(final int x, final int y) {
		return ((9 * y) + x);
	}

	public int getSize() {
		return -1;
	}

	public abstract String getTitle(Player player);

	public abstract Map<Integer, org.imanity.addon.chunkanalyzer.util.menu.Button> getButtons(Player player);

	protected ImmutableMap.Builder<Integer, Button> newMap() {
		return ImmutableMap.builder();
	}

	public void onOpen(final Player player) {
	}

	public void onClose(final Player player) {
	}

}
