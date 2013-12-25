/**
 * Copyright (c) 2010 Peter Major
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.shamble.common.utils;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MeteredInputStream extends FilterInputStream {

    private long counter = 0;

    public MeteredInputStream(InputStream is) {
        super(is);
    }

    @Override
    public int read() throws IOException {
        int read = super.read();
        if (read != -1) {
            counter++;
            checkLimit();
        }
        return read;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = super.read(b, off, len);
        if (read != -1) {
            counter += read;
            checkLimit();
        }
        return read;
    }

    private void checkLimit() throws IOException {
        if (counter > 65535) {
            throw new IOException("Input stream size limit has been exceeded");
        }
    }
}
