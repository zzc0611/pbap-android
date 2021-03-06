package xyz.santeri.pbap.ui.start;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import timber.log.Timber;
import xyz.santeri.pbap.R;
import xyz.santeri.pbap.ui.base.BaseFragment;

/**
 * @author Santeri 'iffa'
 */
public class StartFragment extends BaseFragment {
    public static StartFragment newInstance() {
        return new StartFragment();
    }

    @BindView(R.id.bt_continue)
    Button continueButton;

    @BindView(R.id.tv_faq_permissions_body)
    TextView permissionsText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        permissionsText.setText(Html.fromHtml(getString(R.string.tv_faq_permissions_body)));

        RxPermissions rxPermissions = new RxPermissions(getActivity());
        RxView.clicks(continueButton)
                .compose(rxPermissions.ensure(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS,
                        Manifest.permission.GET_ACCOUNTS))
                .subscribe(granted -> {
                    if (granted) {
                        Timber.d("User granted permissions, continuing");
                        EventBus.getDefault().post(new ContinueClickEvent());
                    } else {
                        Timber.e("User denied permissions, is an idiot");
                    }
                });
    }
}
