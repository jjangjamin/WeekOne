package com.example.q.example;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public ContactsFragment() {
        // Required empty public constructor
    }

    private final static int[] TO_IDS = {
            android.R.id.text1
    };
    ListView mContactsList;
    long mContactId;
    String mContactKey;
    Uri mContactUri;
    private SimpleCursorAdapter mCursorAdapter;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private Button loadContacts;
    private ListView listviewContact;
    private TextView listContacts;
    ArrayList<String> StoreContacts;
    ArrayList<String> StoreContacts2;
    ArrayAdapter<String> arrayAdapter;
    Cursor cursor;
    String name, phonenumber;
    public static final int RequestPermissionCode = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_contacts, container, false);
        listviewContact = (ListView) v.findViewById(R.id.listviewContacts);
        listContacts = (TextView) v.findViewById(R.id.listContacts);
        loadContacts = (Button) v.findViewById(R.id.button);
        StoreContacts = new ArrayList<String>();
        StoreContacts2 = new ArrayList<String>();

        loadContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetContactsIntoArrayList();
                listviewContact.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),
                        R.layout.contact_items_listview, R.id.textView, StoreContacts));
                loadContacts.setEnabled(false);

            }
        });

        listviewContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?>parent, View v, final int position, long id) {
                final String friend = (StoreContacts.get(position).replaceAll("[0-9]", "").replaceAll(" ", "").replaceAll(new String(Character.toChars(0x1F92A)), "")
                        .replaceAll(new String(Character.toChars(0x1F37B)), "").replaceAll(new String(Character.toChars(0x1F4DE)), ""));
                        new AlertDialog.Builder(getActivity()).setTitle("전화 콜!").setMessage(friend + " 한테 술 먹자고 연락해봐요?").setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("전화걸기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String phone = (StoreContacts2.get(position).replaceAll("[^0-9]", ""));
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + phone));
                                try {
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("문자보내기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new AlertDialog.Builder(getActivity()).setTitle("문자보내기").setMessage("직접 보내실래요 아니면 내가 대신 보내드릴까요?").setIcon(android.R.drawable.ic_dialog_email)
                                .setPositiveButton("직접보내기",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String phone = (StoreContacts2.get(position).replaceAll("[^0-9]", ""));
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms",phone,null));
                                        try {
                                            startActivity(intent);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .setNegativeButton("대신 보내줘", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String phone = (StoreContacts2.get(position).replaceAll("[^0-9]", ""));
                                        String messageToSend = "오늘 술 각? 적셔부려 레츠기릿!";
                                        SmsManager.getDefault().sendTextMessage(phone, null, messageToSend, null, null);
                                        Toast.makeText(getActivity(), friend + "한테 문자 보냈쩌여~", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .setNeutralButton("취소", null).show();

                            }
                        })
                        .setNeutralButton("취소", null).show();
            }
        });
        return v;

    }

    public void GetContactsIntoArrayList() {
        cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                StoreContacts.add( new String(Character.toChars(0x1F92A)) + new String(Character.toChars(0x1F37B)) + new String(Character.toChars(0x1F4DE)) + "    " + name );
                StoreContacts2.add( phonenumber);
            }
            cursor.close();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
