package com.tisto.kmp.helper.ui.boilerplate.sample.data

import com.tisto.kmp.helper.ui.boilerplate.sample.presentation.list.SampleListViewModel
import com.tisto.kmp.helper.ui.boilerplate.sample.presentation.form.SampleFormViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

// ══════════════════════════════════════════════════════════════════════════
// KOIN MODULE — satu module file per feature.
// Register di app/.../core/di/ListModules.kt.
//
// Pattern:
//   singleOf(::Api), singleOf(::Repository)
//   viewModelOf(::ListViewModel)
//   Form VM: viewModel { params -> FormViewModel(repo = get(), initialItem = params.getOrNull()) }
// ══════════════════════════════════════════════════════════════════════════

internal val sampleModule = module {
    singleOf(::SampleApi)
    singleOf(::SampleRepository)
    viewModelOf(::SampleListViewModel)
    viewModel { params ->
        SampleFormViewModel(
            repo = get(),
            initialItem = params.getOrNull(),
        )
    }
}
