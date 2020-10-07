package com.cased;

import com.cased.api.Resource;
import com.cased.http.Requestor;

public class Export extends Resource {

    public Export(Requestor requestor) {
        super(requestor, "exports");
    }
}
