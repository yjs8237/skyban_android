package com.nycompany.skybanminitp

/**
 * @author Sylvain Berfini
 */
enum class FragmentsAvailable {
    UNKNOW,
    DIALER,
    EMPTY,
    HISTORY_LIST,
    HISTORY_DETAIL,
    CONTACTS_LIST,
    CONTACT_DETAIL,
    CONTACT_EDITOR,
    ABOUT,
    ACCOUNT_SETTINGS,
    SETTINGS,
    CHAT_LIST,
    CHAT,
    SETUP;

    fun shouldAnimate(): Boolean {
        return true
    }

    fun isRightOf(fragment: FragmentsAvailable): Boolean {
        when (this) {
            HISTORY_LIST -> return fragment == UNKNOW

            HISTORY_DETAIL -> return HISTORY_LIST.isRightOf(fragment) || fragment == HISTORY_LIST

            CONTACTS_LIST -> return HISTORY_DETAIL.isRightOf(fragment) || fragment == HISTORY_DETAIL

            CONTACT_DETAIL -> return CONTACTS_LIST.isRightOf(fragment) || fragment == CONTACTS_LIST

            CONTACT_EDITOR -> return CONTACT_DETAIL.isRightOf(fragment) || fragment == CONTACT_DETAIL

            DIALER -> return CONTACT_EDITOR.isRightOf(fragment) || fragment == CONTACT_EDITOR

            CHAT_LIST -> return DIALER.isRightOf(fragment) || fragment == DIALER

            SETTINGS -> return CHAT_LIST.isRightOf(fragment) || fragment == CHAT_LIST
            ABOUT, ACCOUNT_SETTINGS -> return SETTINGS.isRightOf(fragment) || fragment == SETTINGS
            CHAT -> return CHAT_LIST.isRightOf(fragment) || fragment == CHAT_LIST
            SETUP -> return ABOUT.isRightOf(fragment) || fragment == ABOUT
            else -> return false
        }
    }

    fun shouldAddItselfToTheRightOf(fragment: FragmentsAvailable): Boolean {
        when (this) {
            HISTORY_DETAIL -> return fragment == HISTORY_LIST || fragment == HISTORY_DETAIL

            CONTACT_DETAIL -> return fragment == CONTACTS_LIST || fragment == CONTACT_EDITOR || fragment == CONTACT_DETAIL

            CONTACT_EDITOR -> return fragment == CONTACTS_LIST || fragment == CONTACT_DETAIL || fragment == CONTACT_EDITOR

            CHAT -> return fragment == CHAT_LIST || fragment == CHAT

            else -> return false
        }
    }
}
