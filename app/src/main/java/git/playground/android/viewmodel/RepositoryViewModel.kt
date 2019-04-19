package git.playground.android.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import git.playground.android.datalayer.api.GithubService
import git.playground.android.di.DepGraph
import git.playground.android.domain.SchedulerProvider
import git.playground.android.ui.Adapter
import git.playground.android.ui.Loading
import git.playground.android.ui.RepositoryUiState
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

class RepositoryViewModel: ViewModel() {

    @Inject lateinit var githubService: GithubService
    @Inject lateinit var schedulerProvider: SchedulerProvider
    private val repositories = MutableLiveData<RepositoryUiState>()
    @Volatile private var disposable: Disposable? = null

    init {
        DepGraph.component.inject(this)
    }

    fun getRepositories(): LiveData<RepositoryUiState> {
        return repositories
    }

    fun searchRepository(repositoryName: String) {
        disposable?.dispose()
        repositories.value = Loading

        disposable = githubService.fetchRepositoryList(repositoryName)
            .map { Adapter.map(it) }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ repositories.value = it }, {
                //Handle the error properly
                Timber.e(Log.getStackTraceString(it))
                repositories.value = Adapter.map(it)
            })
    }

    override fun onCleared() {
        disposable?.dispose()
        disposable = null
    }
}