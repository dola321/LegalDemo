package org.sin.legaldemo.NormalUserUI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.sin.legaldemo.Util.Content;
import org.sin.legaldemo.JavaBean.Task;
import org.sin.legaldemo.JavaBean.UserBean;
import org.sin.legaldemo.R;
import org.sin.legaldemo.Util.Utils;

import cn.bmob.v3.BmobACL;
import cn.bmob.v3.listener.SaveListener;

public class TaskPublishFragment extends Fragment {
    private View mView;
    private EditText mTitle;
    private EditText mContent;
    private TextView mType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);   //告知Activity的toolbar添加fragment新增的方法
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();


        mView = inflater.inflate(R.layout.fragment_task_publish, container, false);

        mTitle = (EditText) mView.findViewById(R.id.et_title);
        mContent = (EditText) mView.findViewById(R.id.et_content);
        mType = (TextView) mView.findViewById(R.id.tv_type);

        mType.setText(bundle.get("Task_Type").toString());

        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Content.isTask = false;
        getActivity().invalidateOptionsMenu();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.main_toolbar_settings) {
            return true;
        } else if (id == R.id.main_toolbar_send) {

            UserBean user = UserBean.getCurrentUser(getContext(), UserBean.class);

            Task task = new Task();
            BmobACL acl = new BmobACL();

            acl.setPublicWriteAccess(true);
            acl.setPublicReadAccess(true);

//            acl.setReadAccess(user,true);
//            acl.setWriteAccess(user,true);

            task.setTitle(mTitle.getText().toString().trim());
            task.setShort_content(mContent.getText().toString().trim());
            task.setEvent_type(mType.getText().toString().trim());
            task.setBook(false);
            task.setTask_publisher(user);
            task.setACL(acl);

            task.save(getContext(), new SaveListener() {
                @Override
                public void onSuccess() {
                    Utils.mToast("任务发布成功~请等待律师抢单~");
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment_container, new SelectFragment());
                    transaction.commit();
                }

                @Override
                public void onFailure(int i, String s) {
                    Utils.mToast("发布失败！请稍后重试！"+s);
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }
}
