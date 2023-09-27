package com.example.customcalendar


/**
 * This class represents the property object associated with a description.
 * @author Naishadh Parmar
 * @version 1.0
 * @since 2017-07-14
 */
class Property {
    /**
     * Resource id for the layout to be inflated.
     */
    var layoutResource = -1

    /**
     * Resource id for the text view within the date view which will be used to display day of month.
     */
    var dateTextViewResource = -1

    /**
     * true if the date view should be enabled, false otherwise.
     */
    var enable = true
}