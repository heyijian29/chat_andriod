package sdk.chat.core.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by benjaminsmiley-andrews on 08/05/2017.
 */

public class FileUploadResult {

    public String name;
    public String mimeType;
    public String url;
    public Progress progress = new Progress();
    public Map<String, String> meta = new HashMap<>();

    public boolean urlValid() {
        return url != null && !url.isEmpty();
    }

}
