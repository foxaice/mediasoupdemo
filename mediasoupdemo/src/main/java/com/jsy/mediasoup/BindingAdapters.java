package com.jsy.mediasoup;

import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsy.mediasoup.utils.LogUtils;
import com.jsy.mediasoup.vm.MeProps;

import org.mediasoup.droid.lib.RoomClient;
import org.mediasoup.droid.lib.RoomConstant;
import org.mediasoup.droid.lib.model.DeviceInfo;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoTrack;

public class BindingAdapters {

  private static final String TAG = "BindingAdapters";

  public static void roomState(
          ImageView view, RoomConstant.ConnectionState state, Animation animation) {
    if (state == null) {
      return;
    }
    if (RoomConstant.ConnectionState.CONNECTING.equals(state)) {
      view.setImageResource(R.drawable.ic_state_connecting);
      view.startAnimation(animation);
    } else if (RoomConstant.ConnectionState.CONNECTED.equals(state)) {
      view.setImageResource(R.drawable.ic_state_connected);
      animation.cancel();
      view.clearAnimation();
    } else {
      view.setImageResource(R.drawable.ic_state_new_close);
      animation.cancel();
      view.clearAnimation();
    }
  }

  public static void inviteLink(TextView view, String inviteLink) {
    view.setVisibility(TextUtils.isEmpty(inviteLink) ? View.INVISIBLE : View.VISIBLE);
  }

  public static void hideVideos(ImageView view, boolean audioOnly, boolean audioOnlyInProgress) {
    view.setEnabled(!audioOnlyInProgress);
    if (!audioOnly) {
      view.setBackgroundResource(R.drawable.bg_left_box_off);
      view.setImageResource(R.drawable.icon_video_white_off);
    } else {
      view.setBackgroundResource(R.drawable.bg_left_box_on);
      view.setImageResource(R.drawable.icon_video_black_on);
    }
  }

  public static void audioMuted(ImageView view, boolean audioMuted) {
    if (!audioMuted) {
      view.setBackgroundResource(R.drawable.bg_left_box_off);
      view.setImageResource(R.drawable.icon_volume_white_off);
    } else {
      view.setBackgroundResource(R.drawable.bg_left_box_on);
      view.setImageResource(R.drawable.icon_volume_black_on);
    }
  }

  public static void restartIce(
          ImageView view, boolean restart_ice_in_progress, Animation animation) {
    LogUtils.d(TAG, "restartIce() " + restart_ice_in_progress);
    view.setEnabled(!restart_ice_in_progress);
    if (restart_ice_in_progress) {
      view.startAnimation(animation);
    } else {
      animation.cancel();
      view.clearAnimation();
    }
  }

  public static void deviceInfo(TextView view, DeviceInfo deviceInfo) {
    if (deviceInfo == null) {
      return;
    }

    int deviceIcon = R.drawable.ic_unknown;
    if (!TextUtils.isEmpty(deviceInfo.getFlag())) {
      switch (deviceInfo.getFlag().toLowerCase()) {
        case "chrome":
          deviceIcon = R.mipmap.chrome;
          break;
        case "firefox":
          deviceIcon = R.mipmap.firefox;
          break;
        case "safari":
          deviceIcon = R.mipmap.safari;
          break;
        case "opera":
          deviceIcon = R.mipmap.opera;
          break;
        case "edge":
          deviceIcon = R.mipmap.edge;
          break;
        case "android":
          deviceIcon = R.mipmap.android;
          break;
      }
      view.setText(deviceInfo.getName() + " " + deviceInfo.getVersion());
    } else {
      view.setText("");
    }
    view.setCompoundDrawablesWithIntrinsicBounds(deviceIcon, 0, 0, 0);
  }

  public static void deviceMicState(ImageView imageView, MeProps.DeviceState state) {
    if (state == null) {
      return;
    }
    LogUtils.d(TAG, "edias_mic_state: " + state.name());
    if (MeProps.DeviceState.ON.equals(state)) {
      imageView.setBackgroundResource(R.drawable.bg_media_box_on);
    } else {
      imageView.setBackgroundResource(R.drawable.bg_media_box_off);
    }

    switch (state) {
      case ON:
        imageView.setImageResource(R.drawable.icon_mic_black_on);
        break;
      case OFF:
        imageView.setImageResource(R.drawable.icon_mic_white_off);
        break;
      case UNSUPPORTED:
        imageView.setImageResource(R.drawable.icon_mic_white_unsupported);
        break;
    }
  }

  public static void deviceCamState(ImageView imageView, MeProps.DeviceState state) {
    if (state == null) {
      return;
    }
    LogUtils.d(TAG, "edias_cam_state: " + state.name());
    if (MeProps.DeviceState.ON.equals(state)) {
      imageView.setBackgroundResource(R.drawable.bg_media_box_on);
    } else {
      imageView.setBackgroundResource(R.drawable.bg_media_box_off);
    }

    switch (state) {
      case ON:
        imageView.setImageResource(R.drawable.icon_webcam_black_on);
        break;
      case OFF:
        imageView.setImageResource(R.drawable.icon_webcam_white_off);
        break;
      case UNSUPPORTED:
        imageView.setImageResource(R.drawable.icon_webcam_white_unsupported);
        break;
    }
  }

  public static void changeCamState(View view, MeProps.DeviceState state) {
    if (state == null) {
      return;
    }
    LogUtils.d(TAG, "edias_change_came_state: " + state.name());
    if (MeProps.DeviceState.ON.equals(state)) {
      view.setEnabled(true);
    } else {
      view.setEnabled(false);
    }
  }

  public static void shareState(ImageView imageView, MeProps.DeviceState state) {
    if (state == null) {
      return;
    }
    LogUtils.d(TAG, "edias_share_state: " + state.name());
    if (MeProps.DeviceState.ON.equals(state)) {
      imageView.setBackgroundResource(R.drawable.bg_media_box_on);
    } else {
      imageView.setBackgroundResource(R.drawable.bg_media_box_off);
    }

    if (MeProps.DeviceState.ON.equals(state)) {
      imageView.setEnabled(true);
    } else {
//      imageView.setEnabled(false);
      imageView.setEnabled(true);
    }
  }

  public static boolean render(SurfaceViewRenderer renderer, VideoTrack track, boolean isConnected) {
    LogUtils.d(TAG, "edias_render: " + (track != null && isConnected ? "VISIBLE" : "GONE") + ", isConnected:" + isConnected + ", null == renderer:" + (null == renderer));
    if(null != renderer ) {
      if (track != null && isConnected) {
        track.addSink(renderer);
        renderer.setVisibility(View.VISIBLE);
        return true;
      } else {
        renderer.setVisibility(View.GONE);
        return false;
      }
    }
    return false;
  }

  public static void renderEmpty(View renderer, VideoTrack track, boolean isConnected) {
    LogUtils.d(TAG, "edias_render_empty: " + (track != null && isConnected ? "GONE" : "VISIBLE") + ", isConnected:" + isConnected);
    if (track == null || !isConnected) {
      renderer.setVisibility(View.VISIBLE);
    } else {
      renderer.setVisibility(View.GONE);
    }
  }
}
