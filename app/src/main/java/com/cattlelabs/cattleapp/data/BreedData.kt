package com.cattlelabs.cattleapp.data

data class TranslationEntry(
    val en: String,
    val hi: String,
    val or: String
) {
    fun getFor(languageCode: String): String {
        return when (languageCode) {
            "hi" -> this.hi
            "or" -> this.or
            else -> this.en
        }
    }
}

object BreedData {
    val allSpecies = listOf(
        TranslationEntry(en = "Cow", hi = "गाय", or = "ଗାଈ"),
        TranslationEntry(en = "Buffalo", hi = "भैंस", or = "ମଇଁଷି")
    )

    val allGenders = listOf(
        TranslationEntry(en = "Male", hi = "पुरुष", or = "ପୁରୁଷ"),
        TranslationEntry(en = "Female", hi = "महिला", or = "ମହିଳା")
    )

    val allBreeds = listOf(
        TranslationEntry("Alambadi", "आलंबडी", "ଆଲମ୍ବାଡି"),
        TranslationEntry("Amritmahal", "अमृतमहल", "ଅମୃତମହଲ"),
        TranslationEntry("Ayrshire", "आयरशायर", "ଆୟରଶାୟର୍"),
        TranslationEntry("Banni", "बन्नी", "ବନ୍ନି"),
        TranslationEntry("Bargur", "बरगुर", "ବର୍ଗୁର୍"),
        TranslationEntry("Bhadawari", "भदावरी", "ଭଦାୱରୀ"),
        TranslationEntry("Brown Swiss", "ब्राउन स्विस", "ବ୍ରାଉନ୍ ସ୍ୱିସ୍"),
        TranslationEntry("Dangi", "डांगी", "ଡାଙ୍ଗି"),
        TranslationEntry("Deoni", "देवनी", "ଦେଓନି"),
        TranslationEntry("Gir", "गिर", "ଗିର୍"),
        TranslationEntry("Guernsey", "गर्नजी", "ଗୁର୍ନସି"),
        TranslationEntry("Hallikar", "हल्लीकर", "ହଲିକାର"),
        TranslationEntry("Hariana", "हरियाना", "ହରିଆନା"),
        TranslationEntry("Holstein Friesian", "होलस्टीन फ्रीजियन", "ହୋଲଷ୍ଟାଇନ୍ ଫ୍ରିସିଆନ୍"),
        TranslationEntry("Jaffarabadi", "जाफराबादी", "ଜାଫରାବାଦୀ"),
        TranslationEntry("Jersey", "जर्सी", "ଜର୍ସି"),
        TranslationEntry("Kangayam", "कंगायम", "କାଙ୍ଗାୟାମ"),
        TranslationEntry("Kankrej", "कंकरेज", "କାଙ୍କ୍ରେଜ"),
        TranslationEntry("Kasaragod", "कासरगोड", "କାସରଗଡ଼"),
        TranslationEntry("Kenkatha", "केनकाथा", "କେନକଥା"),
        TranslationEntry("Kherigarh", "खेरीगढ़", "ଖେରୀଗଡ଼"),
        TranslationEntry("Khillari", "खिल्लारी", "ଖିଲାରୀ"),
        TranslationEntry("Krishna Valley", "कृष्णा घाटी", "କୃଷ୍ଣା ଉପତ୍ୟକା"),
        TranslationEntry("Malnad Gidda", "मलनाड गिद्दा", "ମଲନାଡ଼ ଗିଡ୍ଡା"),
        TranslationEntry("Mehsana", "मेहसाणा", "ମେହସାଣା"),
        TranslationEntry("Murrah", "मुर्रा", "ମୁରା"),
        TranslationEntry("Nagori", "नागोरी", "ନାଗୋରୀ"),
        TranslationEntry("Nagpuri", "नागपुरी", "ନାଗପୁରୀ"),
        TranslationEntry("Nili Ravi", "नीली रावी", "ନୀଳି ରବି"),
        TranslationEntry("Nimari", "निमाड़ी", "ନିମାରୀ"),
        TranslationEntry("Ongole", "ओंगोल", "ଓଙ୍ଗୋଲ"),
        TranslationEntry("Pulikulam", "पुलिकुलम", "ପୁଲିକୁଲମ୍"),
        TranslationEntry("Rathi", "राठी", "ରାଠୀ"),
        TranslationEntry("Red Dane", "रेड डेन", "ରେଡ୍ ଡେନ୍"),
        TranslationEntry("Red Sindhi", "लाल सिंधी", "ଲାଲ୍ ସିନ୍ଧି"),
        TranslationEntry("Sahiwal", "साहीवाल", "ସାହିୱାଲ"),
        TranslationEntry("Surti", "सुरती", "ସୁରତୀ"),
        TranslationEntry("Tharparkar", "थारपारकर", "ଥାରପାରକର"),
        TranslationEntry("Toda", "टोडा", "ଟୋଡା"),
        TranslationEntry("Umblachery", "उम्बलाचेरी", "ଉମ୍ବଲାଚେରୀ"),
        TranslationEntry("Vechur", "वेचुर", "ଭେଚୁର")
    )
}