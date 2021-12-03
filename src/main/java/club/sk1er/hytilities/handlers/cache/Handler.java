package club.sk1er.hytilities.handlers.cache;

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
