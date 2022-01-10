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

package org.imanity.addon.chunkanalyzer.data;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.imanity.imanityspigot.ImanitySpigot;
import org.imanity.imanityspigot.chunk.ChunkAnalyse;
import org.imanity.imanityspigot.config.ImanitySpigotWorldConfig;

@Getter
public class ChunkAnalyzeResult {

    private final ChunkAnalyse.SortTarget sortTarget;
    private final ChunkAnalyse.SortMethod sortMethod;
    private final ChunkAnalyse.WorldAnalysesExport export;
    private final long time;
    private final ImanitySpigotWorldConfig worldConfig;

    // NOT USEFUL TO USE MULTIPLE CONSTRUCTOR SINCE WE WANT TO HANDLE IN IF THINGS ARE NULL OR NOT
    public ChunkAnalyzeResult(ChunkAnalyse.SortTarget sortTarget, ChunkAnalyse.SortMethod sortMethod,
                              EntityType entityType, ChunkAnalyse.TileEntityType tileEntityType, World world) {
        this.sortTarget = sortTarget;
        this.sortMethod = sortMethod;

        if (entityType != null) {
            this.export = Bukkit.imanity().getChunkAnalyse().getAnalyseExport(world, sortTarget, sortMethod, entityType);
        } else if (tileEntityType != null) {
            this.export = Bukkit.imanity().getChunkAnalyse().getAnalyseExport(world, sortTarget, sortMethod, tileEntityType);
        } else {
            this.export = Bukkit.imanity().getChunkAnalyse().getAnalyseExport(world, sortTarget, sortMethod);
        }
        this.time = System.currentTimeMillis();
        this.worldConfig = ImanitySpigot.INSTANCE.getWorldConfig(world.getName());
    }

    public WarningType getWarningType(ChunkAnalyse.ChunkAnalysesExport chunk) {
        double potentialLagChunkTickTime = 2.0; // TODO this.worldConfig.getPotentialLagChunkTickTime();
        double average = chunk.getTotal().getAvg();

        return average >= potentialLagChunkTickTime ? WarningType.HIGH
                : average >= potentialLagChunkTickTime * 0.75 ? WarningType.MEDIUM
                : WarningType.LOW;
    }

    @Getter
    public enum WarningType {
        HIGH("&c", Material.REDSTONE_BLOCK),
        MEDIUM("&e", Material.GOLD_BLOCK),
        LOW("&2", Material.EMERALD_BLOCK);

        private final String color;
        private final Material icon;

        WarningType(String color, Material icon) {
            this.color = color;
            this.icon = icon;
        }

    }

}
