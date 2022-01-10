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

package org.imanity.addon.chunkanalyzer.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.World;
import org.imanity.addon.chunkanalyzer.menu.WorldMenu;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ChunkAnalyzerManager {

    private final Cache<World, WorldMenu> menus;

    private long startedAnalyzerRecordTime;
    private long endedAnalyzerRecordTime;

    public ChunkAnalyzerManager() {
        this.menus = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).build();
    }

    public WorldMenu getMenuByWorld(World world) {
        try {
            return menus.get(world, () -> new WorldMenu(this, world));
        } catch (ExecutionException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public long getStartedAnalyzerRecordTime() {
        return this.startedAnalyzerRecordTime;
    }

    public long getEndedAnalyzerRecordTime() {
        return this.endedAnalyzerRecordTime;
    }

    public void setEndedAnalyzerRecordTime(long endedAnalyzerRecordTime) {
        this.endedAnalyzerRecordTime = endedAnalyzerRecordTime;
    }

    public void setStartedAnalyzerRecordTime(long startedAnalyzerRecordTime) {
        this.startedAnalyzerRecordTime = startedAnalyzerRecordTime;
    }
}
