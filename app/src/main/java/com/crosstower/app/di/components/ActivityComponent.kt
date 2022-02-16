package com.crosstower.app.di.components

import com.crosstower.app.di.scopes.ActivityScope
import com.crosstower.app.ui.AlbumDetailsActivity
import dagger.Component

/***
 * Activity component for DI
 */
@ActivityScope
@Component(dependencies = [AppComponent::class])
interface ActivityComponent {

    fun inject(albumDetailsActivity: AlbumDetailsActivity)
}