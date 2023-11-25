# react-native-android-navbar

react-native-android-navbar is a lightweight and efficient React Native package that provides a simple interface to customize the navigation bar on Android devices. It leverages the native Android API to allow for a seamless integration into your React Native project, enabling full control over the navigation bar appearance, including its color and visibility.

## Installation

```sh
npm install react-native-android-navbar
```

## Usage

```js
import {
  changeNavigationBarColor,
  hideNavigationBar,
  showNavigationBar,
} from 'react-native-android-navbar';

// ...

const setNavBarColor = async (navColor: string) => {
  const response = await changeNavigationBarColor(navColor, true, false);
};

<Button title="Show Navigation Bar" onPress={() => setNavBarColor('blue')} />

<Button title="Show Navigation Bar" onPress={showNavigationBar} />

<Button title="Hide Navigation Bar" onPress={hideNavigationBar} />

```

## Credits

This turbo module package is based on [react-native-navigation-bar-color](https://github.com/thebylito/react-native-navigation-bar-color) to support new react-native architecture.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
