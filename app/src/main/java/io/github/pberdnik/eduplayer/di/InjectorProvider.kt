package io.github.pberdnik.eduplayer.di

import android.app.Activity
import androidx.fragment.app.Fragment

interface InjectorProvider {
  val component: AppComponent
}

val Activity.injector get() = (application as InjectorProvider).component

val Fragment.injector get() = (requireNotNull(activity).application as InjectorProvider).component