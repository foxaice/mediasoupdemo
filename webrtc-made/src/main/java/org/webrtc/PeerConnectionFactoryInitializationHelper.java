/*
 *  Copyright (c) 2018 The WebRTC project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a BSD-style license
 *  that can be found in the LICENSE file in the root of the source
 *  tree. An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */

package org.webrtc;

import android.content.Context;

import org.webrtc.PeerConnectionFactory.InitializationOptions;

public class PeerConnectionFactoryInitializationHelper {
  private static class MockLoader implements NativeLibraryLoader {
    @Override
    public boolean load(String name) {
      return true;
    }
  }

  @CalledByNative
  public static void initializeFactoryForTests() {
    Context ctx = ContextUtils.getApplicationContext();
    InitializationOptions options = InitializationOptions.builder(ctx)
                                        .setNativeLibraryLoader(new MockLoader())
                                        .createInitializationOptions();

    PeerConnectionFactory.initialize(options);
  }
}
