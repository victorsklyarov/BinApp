package com.zeroillusion.binapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.room.Room
import com.zeroillusion.binapp.api.BinApi
import com.zeroillusion.binapp.data.BinItem
import com.zeroillusion.binapp.data.Database
import com.zeroillusion.binapp.databinding.FragmentBinBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

class BinFragment : Fragment() {

    private lateinit var db: Database
    private val binViewModel: BinViewModel by activityViewModels()
    private var _binding: FragmentBinBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Room.databaseBuilder(
            requireContext(),
            Database::class.java, "db"
        ).build()

        val binDao = db.binDao()
        CoroutineScope(Dispatchers.Default).launch {
            binViewModel.setListBins(binDao.getBins())
        }

        binding.checkBtn.setOnClickListener {
            binding.inputBin.clearFocus()
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)

            if (binding.inputBin.text.length > 3) {
                getBin(binViewModel.inputString.value.toString())
                CoroutineScope(Dispatchers.Default).launch {
                    binDao.insert(BinItem(0, binViewModel.inputString.value.toString()))
                    binViewModel.setListBins(binDao.getBins())
                    requireActivity().runOnUiThread {
                        binding.inputBin.text.clear()
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.warning_length),
                    Toast.LENGTH_SHORT
                ).show()
                binding.inputBin.text.clear()
            }
        }

        binding.phone.setOnClickListener {
            if (binViewModel.phone.value != "null") {
                dialPhoneNumber(binViewModel.phone.value.toString())
            }
        }

        binding.url.setOnClickListener {
            if (binViewModel.url.value != "null") {
                openWebPage(binViewModel.url.value.toString())
            }
        }

        binding.coordinates.setOnClickListener {
            if ((binViewModel.latitude.value != "null")
                and (binViewModel.longitude.value != "null")
            ) {
                showMap(
                    Uri.parse(
                        resources.getString(
                            R.string.add_geo_uri,
                            binViewModel.latitude.value.toString(),
                            binViewModel.longitude.value.toString()
                        )
                    )
                )
            }
        }

        binding.inputBin.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                binViewModel.setInputString(s)
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })

        val schemeObserver = Observer<String> { out ->
            binding.scheme.text = out
        }
        binViewModel.scheme.observe(requireActivity(), schemeObserver)

        val brandObserver = Observer<String> { out ->
            binding.brand.text = out
        }
        binViewModel.brand.observe(requireActivity(), brandObserver)

        val lengthObserver = Observer<String> { out ->
            binding.length.text = out
        }
        binViewModel.length.observe(requireActivity(), lengthObserver)

        val luhnObserver = Observer<String> { out ->
            binding.luhn.text = out
        }
        binViewModel.luhn.observe(requireActivity(), luhnObserver)

        val typeObserver = Observer<String> { out ->
            binding.type.text = out
        }
        binViewModel.type.observe(requireActivity(), typeObserver)

        val prepaidObserver = Observer<String> { out ->
            binding.prepaid.text = out
        }
        binViewModel.prepaid.observe(requireActivity(), prepaidObserver)

        val countryObserver = Observer<String> { out ->
            binding.country.text = out
        }
        binViewModel.country.observe(requireActivity(), countryObserver)

        val coordinatesObserver = Observer<String> { out ->
            binding.coordinates.text = out
        }
        binViewModel.coordinates.observe(requireActivity(), coordinatesObserver)

        val bankObserver = Observer<String> { out ->
            binding.bank.text = out
        }
        binViewModel.bank.observe(requireActivity(), bankObserver)

        val urlObserver = Observer<String> { out ->
            binding.url.text = out
        }
        binViewModel.url.observe(requireActivity(), urlObserver)

        val phoneObserver = Observer<String> { out ->
            binding.phone.text = out
        }
        binViewModel.phone.observe(requireActivity(), phoneObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        db.close()
    }

    private fun getBin(bin: String) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                updateUI(
                    Bin.getInstance()
                        .create(BinApi::class.java)
                        .getBin(bin)
                        .execute()
                        .body()
                )
            } catch (e: SSLHandshakeException) {
                updateUI(
                    Bin.getInstanceUnsafe()
                        .create(BinApi::class.java)
                        .getBin(bin)
                        .execute()
                        .body()
                )
            } catch (e: UnknownHostException) {
                showError()
            } catch (e: Exception) {
                showError(e)
            }
        }
    }

    private fun updateUI(binItem: BinItem?) {
        if (binItem == null) {
            requireActivity().runOnUiThread {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.warning_bin),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        if (binItem?.scheme != null) {
            binViewModel.setScheme(binItem.scheme.toString())
        } else {
            binViewModel.setScheme("")
        }
        if (binItem?.brand != null) {
            binViewModel.setBrand(binItem.brand.toString())
        } else {
            binViewModel.setBrand("")
        }
        if (binItem?.number?.length != null) {
            binViewModel.setLength(binItem.number?.length.toString())
        } else {
            binViewModel.setLength("")
        }
        if (binItem?.number?.luhn != null) {
            binViewModel.setLuhn(binItem.number?.luhn.toString())
        } else {
            binViewModel.setLuhn("")
        }
        if (binItem?.type != null) {
            binViewModel.setType(binItem.type.toString())
        } else {
            binViewModel.setType("")
        }
        if (binItem?.prepaid != null) {
            binViewModel.setPrepaid(binItem.prepaid.toString())
        } else {
            binViewModel.setPrepaid("")
        }
        if ((binItem?.country?.emoji != null)
            and (binItem?.country?.alpha2 != null)
            and (binItem?.country?.name != null)
        ) {
            binViewModel.setCountry(
                resources.getString(
                    R.string.country_format,
                    binItem?.country?.emoji.toString(),
                    binItem?.country?.alpha2.toString(),
                    binItem?.country?.name.toString()
                )
            )
        } else {
            binViewModel.setCountry("")
        }
        if ((binItem?.country?.latitude != null) and (binItem?.country?.longitude != null)) {
            binViewModel.setCoordinates(
                resources.getString(
                    R.string.coordinates,
                    binItem?.country?.latitude.toString(),
                    binItem?.country?.longitude.toString()
                )
            )
        } else {
            binViewModel.setCoordinates("")
        }
        if ((binItem?.bank?.name != null) and (binItem?.bank?.city != null)) {
            binViewModel.setBank(
                resources.getString(
                    R.string.bank_format,
                    binItem?.bank?.name.toString(),
                    binItem?.bank?.name.toString()
                )
            )
        } else if (binItem?.bank?.name != null) {
            binViewModel.setBank(binItem.bank?.name.toString())
        } else {
            binViewModel.setBank("")
        }
        if (binItem?.bank?.url != null) {
            binViewModel.setUrl(binItem.bank?.url.toString())
        } else {
            binViewModel.setUrl("")
        }
        if (binItem?.bank?.phone != null) {
            binViewModel.setPhone(binItem.bank?.phone.toString())
        } else {
            binViewModel.setPhone("")
        }
        if (binItem?.country?.latitude != null) {
            binViewModel.setLatitude(binItem.country?.latitude.toString())
        } else {
            binViewModel.setLatitude("")
        }
        if (binItem?.country?.longitude != null) {
            binViewModel.setLongitude(binItem.country?.longitude.toString())
        } else {
            binViewModel.setLongitude("")
        }
    }

    private fun dialPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse(resources.getString(R.string.add_tel_uri, phoneNumber))
        }
        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.warning_app),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openWebPage(url: String) {
        val webpage: Uri = Uri.parse(resources.getString(R.string.add_https, url))
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.warning_app),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showMap(geoLocation: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = geoLocation
        }
        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.warning_app),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showError(e: Exception? = null) {
        requireActivity().runOnUiThread {
            Toast.makeText(
                requireContext(),
                e?.toString() ?: resources.getString(R.string.warning_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }
}