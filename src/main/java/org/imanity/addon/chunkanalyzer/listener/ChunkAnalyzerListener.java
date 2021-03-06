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

package org.imanity.addon.chunkanalyzer.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.imanity.addon.chunkanalyzer.manager.ChunkAnalyzerManager;
import org.imanity.imanityspigot.event.chunk.ChunkAnalyseEndEvent;
import org.imanity.imanityspigot.event.chunk.ChunkAnalyseStartEvent;

public class ChunkAnalyzerListener implements Listener {

    private final ChunkAnalyzerManager manager;

    public ChunkAnalyzerListener(ChunkAnalyzerManager manager) {
        this.manager = manager;
    }

    @EventHandler
    private void onChunkAnalyseStart(ChunkAnalyseStartEvent event) {
        this.manager.setStartedAnalyzerRecordTime(System.currentTimeMillis());
    }

    @EventHandler
    private void onChunkAnalyzeEnd(ChunkAnalyseEndEvent event) {
        this.manager.setEndedAnalyzerRecordTime(System.currentTimeMillis());
    }
}
