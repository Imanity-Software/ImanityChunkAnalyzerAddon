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

import lombok.Getter;
import org.imanity.addon.chunkanalyzer.util.menu.Button;
import org.imanity.addon.chunkanalyzer.util.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class PaginatedMenu extends Menu {

	@Getter
	private int page = 1;
	{
		setUpdateAfterClick(false);
	}

	@Override
	public String getTitle(Player player) {
		return getPrePaginatedTitle(player) + " - " + this.page + "/" + getPages(player);
	}

	/**
	 * Changes the page number
	 *
	 * @param player player viewing the inventory
	 * @param mod    delta to modify the page number by
	 */
	public final void modPage(Player player, int mod) {
		this.page += mod;
		getButtons().clear();
		openMenu(player);
	}

	/**
	 * @param player player viewing the inventory
	 */
	public final int getPages(Player player) {
		int buttonAmount = getAllPagesButtons(player).size();

		if (buttonAmount == 0) {
			return 1;
		}

		return (int) Math.ceil(buttonAmount / (double) getMaxItemsPerPage(player));
	}

	@Override
	public final Map<Integer, Button> getButtons(Player player) {
		int minIndex = (int) ((double) (this.page - 1) * getMaxItemsPerPage(player));
		int maxIndex = (int) ((double) (this.page) * getMaxItemsPerPage(player));

		Map<Integer, Button> buttons = new HashMap<>();

		buttons.put(0, new PageButton(-1, this));
		buttons.put(8, new PageButton(1, this));

		for (Map.Entry<Integer, Button> entry : getAllPagesButtons(player).entrySet()) {
			int ind = entry.getKey();

			if (ind >= minIndex && ind < maxIndex) {
				ind -= (int) ((double) (getMaxItemsPerPage(player)) * (this.page - 1)) - 9;
				buttons.put(ind, entry.getValue());
			}
		}

		Map<Integer, Button> global = getGlobalButtons(player);

		if (global != null) {
			buttons.putAll(global);
		}
		return buttons;
	}

	public int getMaxItemsPerPage(Player player) {
		return 18;
	}

	/**
	 * @param player player viewing the inventory
	 *
	 * @return a Map of buttons that returns items which will be present on all pages
	 */
	public Map<Integer, Button> getGlobalButtons(Player player) {
		return null;
	}

	/**
	 * @param player player viewing the inventory
	 *
	 * @return title of the inventory before the page number is added
	 */
	public abstract String getPrePaginatedTitle(Player player);

	/**
	 * @param player player viewing the inventory
	 *
	 * @return a map of buttons that will be paginated and spread across pages
	 */
	public abstract Map<Integer, Button> getAllPagesButtons(Player player);

}
