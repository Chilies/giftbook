package edu.sctu.giftbook.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.sctu.giftbook.entity.Contact;

/**
 * Created by zhengsenwen on 2018/4/12.
 */

public class ContactUtil {

    /**
     * 获取手机通讯录信息（ID，姓名，电话号码）
     * @param activity
     * @return
     */
    public static List<Contact> getContactInfo(Activity activity) {
        List<Contact> contactList = new ArrayList<>();
        ContentResolver contentResolver = activity.getContentResolver();
        Cursor cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            if (hasPhone.equalsIgnoreCase("1")) {
                hasPhone = "true";
            } else {
                hasPhone = "false";
            }
            if (Boolean.parseBoolean(hasPhone)) {
                Cursor phones = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                + " = " + contactId, null, null);
                while (phones.moveToNext()) {
                    String phoneNumber = phones.getString(phones.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phoneNumber = phoneNumber.replaceAll(" ", "").replaceAll("-", "");
                    if (phoneNumber.length() == 11 && phoneNumber.startsWith("1")) {
                        Log.e("contact", "contactId=" + contactId + "name=" + name + "phoneNumber" + phoneNumber);

                        Contact contact = new Contact();
                        contact.setContactId(contactId);
                        contact.setName(name);
                        contact.setPhoneNumber(phoneNumber);
                        contactList.add(contact);
                    }
                }
            }
        }

        return contactList;
    }



}
