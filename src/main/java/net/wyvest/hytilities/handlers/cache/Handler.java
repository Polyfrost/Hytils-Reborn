/*
 * Hytilities Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2021  W-OVERFLOW
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.wyvest.hytilities.handlers.cache;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//TODO: needs a cleanup idk what im doing
public abstract class Handler<A> {
    public static Handler INSTANCE;
    public JsonObject jsonObject = null;
    public final JsonParser parser = new JsonParser();

    /**
     * Initializes the handler.
     */
    public abstract void initialize();

}
