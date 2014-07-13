# Go-CD Simple Email Task plugin

Now sending emails from Go is so simple. 

### Setup 
- Download the [`go-cd-simple-email-task-plugin-1.1.jar`](https://github.com/ashwanthkumar/go-cd-simple-email-task-plugin/releases/download/v1.1/go-cd-simple-email-task-plugin-1.1.jar)
- Put it inside your $GO_SERVER/plugins/external
- Restart your Go Server

This plugin uses `mail` command to send emails. It is assumed the `mail` is configured on all your agent machines.

### What do you get?

- EMail Task

![EMail Task Dropdown](https://raw.githubusercontent.com/ashwanthkumar/go-cd-simple-email-task-plugin/master/assets/email_task_dropdown.png)

- Neat Popup

![EMail Task Popup](https://github.com/ashwanthkumar/go-cd-simple-email-task-plugin/raw/master/assets/email_task_popup.png)

- Whola! You get the email in your inbox.

![EMail Received](https://raw.githubusercontent.com/ashwanthkumar/go-cd-simple-email-task-plugin/master/assets/email_with_attachments.png)


### Notes

This plugin uses the `mail` available on Linux / Mac to send emails, so no Windows for now. I am not aware of how to get the server settings from the plugin so that we can use existing configured settings to send email.
