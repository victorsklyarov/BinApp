package com.zeroillusion.binapp

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BinViewModel : ViewModel() {

    private val _inputString: MutableLiveData<Editable> by lazy { MutableLiveData<Editable>() }
    val inputString: LiveData<Editable> = _inputString
    fun setInputString(item: Editable) {
        _inputString.value = item
    }

    private val _scheme: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val scheme: LiveData<String> = _scheme
    fun setScheme(item: String) {
        _scheme.postValue(item)
    }

    private val _brand: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val brand: LiveData<String> = _brand
    fun setBrand(item: String) {
        _brand.postValue(item)
    }

    private val _length: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val length: LiveData<String> = _length
    fun setLength(item: String) {
        _length.postValue(item)
    }

    private val _luhn: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val luhn: LiveData<String> = _luhn
    fun setLuhn(item: String) {
        _luhn.postValue(item)
    }

    private val _type: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val type: LiveData<String> = _type
    fun setType(item: String) {
        _type.postValue(item)
    }

    private val _prepaid: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val prepaid: LiveData<String> = _prepaid
    fun setPrepaid(item: String) {
        _prepaid.postValue(item)
    }

    private val _country: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val country: LiveData<String> = _country
    fun setCountry(item: String) {
        _country.postValue(item)
    }

    private val _coordinates: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val coordinates: LiveData<String> = _coordinates
    fun setCoordinates(item: String) {
        _coordinates.postValue(item)
    }

    private val _latitude: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val latitude: LiveData<String> = _latitude
    fun setLatitude(item: String) {
        _latitude.postValue(item)
    }

    private val _longitude: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val longitude: LiveData<String> = _longitude
    fun setLongitude(item: String) {
        _longitude.postValue(item)
    }

    private val _bank: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val bank: LiveData<String> = _bank
    fun setBank(item: String) {
        _bank.postValue(item)
    }

    private val _url: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val url: LiveData<String> = _url
    fun setUrl(item: String) {
        _url.postValue(item)
    }

    private val _phone: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val phone: LiveData<String> = _phone
    fun setPhone(item: String) {
        _phone.postValue(item)
    }

    private val _listBins: MutableLiveData<Array<String>> by lazy { MutableLiveData<Array<String>>(arrayOf("")) }
    val listBins: LiveData<Array<String>> = _listBins
    fun setListBins(item: Array<String>) {
        _listBins.postValue(item)
    }
}