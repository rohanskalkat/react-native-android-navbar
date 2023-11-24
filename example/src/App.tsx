import React, { useEffect, useState } from 'react';
import {
  StyleSheet,
  View,
  Text,
  StatusBar,
  TouchableOpacity,
} from 'react-native';
import {
  changeNavigationBarColor,
  hideNavigationBar,
  showNavigationBar,
} from 'react-native-android-navbar';

export default function App() {
  const colors = ['red', 'green', 'blue', 'yellow'];

  const [color, setColor] = useState(colors[0]);

  const getNextColor = () => {
    setColor((currentColor) => {
      let nextColor;
      do {
        // Generate a random index for the next color
        const nextIndex = Math.floor(Math.random() * colors.length);
        // Get the next color using the random index
        nextColor = colors[nextIndex];
      } while (nextColor === currentColor); // Ensure it's not the same as the current color

      return nextColor;
    });
  };

  useEffect(() => {
    if (color) {
      setNavBarColor(color);
    }
  }, [color]);

  const setNavBarColor = async (navColor: string) => {
    try {
      const response = await changeNavigationBarColor(navColor, true, false);
      console.log(response); // {success: true}
    } catch (e) {
      console.log(e); // {success: false}
    }
  };

  return (
    <View style={[styles.container, { backgroundColor: color }]}>
      <StatusBar backgroundColor={color} />
      <Text style={styles.text}>The background color is {color}.</Text>
      <TouchableOpacity onPress={getNextColor} style={styles.button}>
        <Text style={styles.buttonText}>Change Color</Text>
      </TouchableOpacity>
      <TouchableOpacity
        onPress={() => showNavigationBar()}
        style={styles.button}
      >
        <Text style={styles.buttonText}>Show Navigation Bar</Text>
      </TouchableOpacity>
      <TouchableOpacity
        onPress={() => hideNavigationBar()}
        style={styles.button}
      >
        <Text style={styles.buttonText}>Hide Navigation Bar</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  text: {
    color: 'white',
    fontSize: 20,
    marginBottom: 20,
  },
  button: {
    backgroundColor: 'white',
    padding: 10,
    marginVertical: 5,
    borderRadius: 5,
  },
  buttonText: {
    color: 'black',
  },
});
