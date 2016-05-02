package it.cnr.iit.sessionmanagermobile;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import java.util.LinkedList;
import java.util.List;

import it.cnr.iit.sessionmanagerdalutilities.Session;
import it.cnr.iit.sessionmanagerdalutilities.SessionManager;

public class SessionManagerActivity extends OrmLiteBaseActivity<DatabaseMobileHelper> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManagerMobile sm = new SessionManagerMobile();
        sm.onCreate();
        //SessionManagerMobile sm = new SessionManagerMobile("android:sqlite:session_manager.db");
        List<String> attributes = new LinkedList<String>();
        attributes.add("aaa");
        attributes.add("bbb");

        //ESEMPIO 1: CREA TRE SESSIONI, AGGIORNA LO STATO DI UNA, TESTA I METODI GETSESSIONSFORSTATUS E GETSESSIONFOR ID E LE ELIMINA
        /*sm.createEntryForSubject("1", "<policyOnGoing1>", "<policyPost1>", "<originalRequest1>", attributes, "status1", "pepURI1", "fabio");
        sm.createEntryForObject("2", "<policyOnGoing1>", "<policyPost1>", "<originalRequest1>", attributes, "status1", "pepURI1", "room");
        sm.createEntryForObject("3", "<policyOnGoing1>", "<policyPost1>", "<originalRequest1>", attributes, "status1", "pepURI1", "desk");
        sm.updateEntry("1", "status2");
        List<Session> result = sm.getSessionsForStatus("status1");
        for (Session s : result) {
            Log.i(getClass().getSimpleName(), s.toString());
        }
        sm.deleteEntry("1");
        sm.deleteEntry("2");
        sm.deleteEntry("3");*/

        //ESEMPIO 2: CREA 3 SESSIONI CON ON_GOING_ATTRIBUTES MULTI-VALORE E RICHIAMA LA GETSESSIONSFOROBJECTSATTRIBUTES INVIANDOLE 1 ATTRIBUTO
        sm.createEntryForSubject("1", "<policyOnGoing1>", "<policyPost1>", "<originalRequest1>", attributes, "status1", "pepURI1", "fabio");
        sm.createEntryForObject("2", "<policyOnGoing1>", "<policyPost1>", "<originalRequest1>", attributes, "status1", "pepURI1", "room");
        sm.createEntryForObject("3", "<policyOnGoing1>", "<policyPost1>", "<originalRequest1>", attributes, "status1", "pepURI1", "desk");
        sm.createEntryForObject("4", "<policyOnGoing1>", "<policyPost1>", "<originalRequest1>", attributes, "status1", "pepURI1", "desk");
        List<Session> result = sm.getSessionsForObjectAttributes("aaa");
        for (Session s : result) {
            System.out.println(s.toString());
        }
        List<Session> result1 = sm.getSessionsForObjectAttributes("desk","aaa");
        for (Session s : result1) {
            System.out.println(s.toString());
        }
        sm.deleteEntry("1");
        sm.deleteEntry("2");
        sm.deleteEntry("3");
        sm.deleteEntry("4");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_session_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
