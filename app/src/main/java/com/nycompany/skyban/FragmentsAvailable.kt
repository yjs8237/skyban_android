package com.nycompany.skybanminitp

import com.nycompany.skyban.R

/**
 * @author Sylvain Berfini
 */
enum class FragmentsAvailable {
    INORDER,
    OUTORDER,
    EMPTY,
    INFO,
    CHARGE,
    SETTING;
}

enum class ResCode(val Code:String) {
    Success("0"),
    TransmissionError("-101"),
    NoPresent("-201"),
    AlreadyMember("-202"),
    NoSuggested("-203"),
    NoPassword ("-204"),
    NoQNAItem("-301"),
    OrderAlreadyDeleted("-302"),
    AlreadyMatching("-303"),
    NoInOrder("-304"),
    NoOutOrder("-305"),
}
