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

package org.imanity.addon.chunkanalyzer.util.item;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder implements Cloneable {

    private final ItemStack itemStack;

    public ItemBuilder(final Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder(final ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilder amount(final int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder name(final String name) {
        final ItemMeta meta = this.itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(final Iterable<String> lore) {
        final ItemMeta meta = this.itemStack.getItemMeta();

        List<String> toSet = meta.getLore();
        if (toSet == null) {
            toSet = new ArrayList<>();
        }

        for (final String string : lore) {
            toSet.add(ChatColor.translateAlternateColorCodes('&', string));
        }

        meta.setLore(toSet);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(final String... lore) {
        final ItemMeta meta = this.itemStack.getItemMeta();

        List<String> toSet = meta.getLore();
        if (toSet == null) {
            toSet = new ArrayList<>();
        }

        for (final String string : lore) {
            toSet.add(ChatColor.translateAlternateColorCodes('&', string));
        }

        meta.setLore(toSet);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder durability(final int durability) {
        this.itemStack.setDurability((short) - (durability - this.itemStack.getType().getMaxDurability()));
        return this;
    }

    public ItemBuilder data(final int data) {
        this.itemStack.setDurability((short) data);
        return this;
    }

    public ItemBuilder enchantment(final Enchantment enchantment, final int level) {
        this.itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder enchantment(final Enchantment enchantment) {
        this.itemStack.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder type(final Material material) {
        this.itemStack.setType(material);
        return this;
    }

    public ItemBuilder clearLore() {
        final ItemMeta meta = this.itemStack.getItemMeta();
        meta.setLore(new ArrayList<String>());
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder clearEnchantments() {
        for (final Enchantment enchantment : this.itemStack.getEnchantments().keySet()) {
            this.itemStack.removeEnchantment(enchantment);
        }
        return this;
    }

    public ItemBuilder color(final Color color) {
        if (this.itemStack.getType() == Material.LEATHER_BOOTS || this.itemStack.getType() == Material.LEATHER_CHESTPLATE || this.itemStack.getType() == Material.LEATHER_HELMET
                || itemStack.getType() == Material.LEATHER_LEGGINGS) {
            final LeatherArmorMeta meta = (LeatherArmorMeta) this.itemStack.getItemMeta();
            meta.setColor(color);
            this.itemStack.setItemMeta(meta);
            return this;
        } else
            throw new IllegalArgumentException("color() only applicable for leather armor!");
    }

    public ItemBuilder skull(String owner) {
        if (this.itemStack.getType() == Material.SKULL_ITEM && this.itemStack.getDurability() == (byte) 3) {
            SkullMeta skullMeta = (SkullMeta) this.itemStack.getItemMeta();
            skullMeta.setOwner(owner);
            this.itemStack.setItemMeta(skullMeta);
            return this;
        } else {
            throw new IllegalArgumentException("skull() only applicable for human skull item!");
        }
    }

    public ItemBuilder shiny() {
        ItemMeta meta = this.itemStack.getItemMeta();

        meta.addEnchant(Enchantment.PROTECTION_FIRE, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder itemFlag(ItemFlag itemFlag) {
        ItemMeta im = this.itemStack.getItemMeta();
        im.addItemFlags(itemFlag);
        this.itemStack.setItemMeta(im);
        return this;
    }

    public ItemBuilder removeItemFlag(ItemFlag itemFlag) {
        ItemMeta im = this.itemStack.getItemMeta();
        if (im.hasItemFlag(itemFlag)) {
            im.removeItemFlags(itemFlag);
        }
        this.itemStack.setItemMeta(im);
        return this;
    }

    @Override
    public ItemBuilder clone() {
        return new ItemBuilder(this.itemStack.clone());
    }

    public ItemStack build() {
        return this.itemStack;
    }

    public Material getType() {
        return this.itemStack.getType();
    }

}
