# buttonToAction

“Button to Action” app
Mobile app development exam
In this exam, you are asked to write the “Button to Action” mobile app.
The app
This app should have only 1 button. Clicking on this button shall perform one of the actions
below. The app shall choose which action to perform according to a few conditions.
These conditions shall be configurable. The configuration of the actions is specified at the end
of the document.
The button should be located in the center of the screen and should stay that way for as many
different devices as possible.
The actions
The app should support the following actions:
1. Animation action: Animate the button to perform a 360 rotation.
2. Toast message action: Show a Toast message saying “Action is Toast!”

3. Call action: Open a “choose contact” screen. Choosing a contact shows a Toast
message saying “Calling {{contact_name or contact number}}”
4. Notification action: Push a notification with the text “Action Notification!”
Clicking on that notification performs the same as the “Call action” (Open a “choose
contact” screen. Choosing a contact shows a Toast message with that contact’s name
and/or number).
* Keep in mind that in the future, the app should be able to support actions that are more
complex than the actions above.
Choosing an action
When the user clicks on the button, the action that is both suitable and has the highest priority
will be executed. (a suitable action is an action that passes all validations/conditions mentioned
below).
Config values that apply to all actions
The way the action is chosen is according to the configuration values retrieved from the
configuration JSON below.
The configuration values:
● enabled (boolean)
○ a disabled action can never be chosen
● priority (int)
○ if action X has a higher priority than action Y, action X will be selected
○ If two actions have the same priority, choose one at random.
● cool down period (long)
○ if an action has a 3-day cool-down period, it can’t be chosen for 3 days after it
was last performed by the app.
○ BONUS: the cooldown should be persistent and be remembered even if the app
is stopped.

● BONUS: valid days (days array)
○ valid weekdays to choose the action.
For example, we can set the “Animation action” action to be choosable only on
Monday - Thursday.
