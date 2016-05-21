package com.cs442.Team14;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * Invite Friends Fragment
 * @purpose Allows User to Invite Friends and also send personalized message
 * @author PraveenKumar
 * @since 13-Apr-2016
 */
public class InviteFriendsFragment extends Fragment {
    /*
    * Field  for Message to be sent to Friend
    * Enables Personal Message to be typed by the user
    * Enables Personal Message to be typed by the user
     */
    EditText Personalmessage;
    //private OnFragmentInteractionListener mListener;

    public InviteFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view1 = inflater.inflate(R.layout.fragment_invitefriends, container, false);

        Personalmessage=(EditText)view1.findViewById(R.id. message);

        view1.findViewById(R.id.invitefriendbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = Personalmessage.getText().toString();

                /*
                * Invokes Share Intent Which Enables User To share or sennd Invites to Friends
                 */

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Goshop Invite");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });
        return view1;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().setTitle(R.string.title_fragment_inviteFriend);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

}
