import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-android-navbar' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

// @ts-expect-error
const isTurboModuleEnabled = global.__turboModuleProxy != null;

const AndroidNavbarModule = isTurboModuleEnabled
  ? require('./NativeAndroidNavbar').default
  : NativeModules.AndroidNavbar;

const AndroidNavbar = AndroidNavbarModule
  ? AndroidNavbarModule
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function changeNavigationBarColor(
  color: string,
  light: boolean = true,
  animated: boolean = true
): Promise<boolean> {
  if (Platform.OS === 'android') {
    const LightNav = light ? true : false;
    return AndroidNavbar.changeNavigationBarColor(color, LightNav, animated);
  } else {
    return Promise.resolve(false);
  }
}

export function hideNavigationBar(): Promise<boolean> {
  if (Platform.OS === 'android') {
    return AndroidNavbar.hideNavigationBar();
  } else {
    return Promise.resolve(false);
  }
}

export function showNavigationBar(): Promise<boolean> {
  if (Platform.OS === 'android') {
    return AndroidNavbar.showNavigationBar();
  } else {
    return Promise.resolve(false);
  }
}
