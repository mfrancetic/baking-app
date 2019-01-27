//package com.example.android.bakingapp.data;
//
//import android.content.Context;
//import android.content.Intent;
//import android.widget.RemoteViews;
//import android.widget.RemoteViewsService;
//
//import com.example.android.bakingapp.R;
//import com.example.android.bakingapp.models.Ingredient;
//
//import java.util.List;
//
//public class WidgetRemoteViewsService extends RemoteViewsService {
//
//
//    @Override
//    public RemoteViewsFactory onGetViewFactory(Intent intent) {
//        return new WidgetRemoteViewsFactory(this.getApplicationContext());
//    }
//
//    class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
//
//        Context context;
//
//        String recipeName;
//
//        List<String> recipeNames;
//
//        public WidgetRemoteViewsFactory(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public void onCreate() {
//        }
//
//        @Override
//        public void onDataSetChanged() {
//
//        }
//
//        @Override
//        public void onDestroy() {
//
//        }
//
//        @Override
//        public int getCount() {
//            if (recipeNames == null) {
//                return 0;
//            } else {
//                return recipeNames.size();
//            }
//        }
//
//        @Override
//        public RemoteViews getViewAt(int position) {
//
////            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
//
//            return null;
//        }
//
//        @Override
//        public RemoteViews getLoadingView() {
//
//
//            return null;
//        }
//
//        @Override
//        public int getViewTypeCount() {
//            return 0;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return false;
//        }
//    }
//
//}
