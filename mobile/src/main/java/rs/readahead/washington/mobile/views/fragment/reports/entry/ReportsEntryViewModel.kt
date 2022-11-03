package rs.readahead.washington.mobile.views.fragment.reports.entry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hzontal.tella_vault.VaultFile
import dagger.hilt.android.lifecycle.HiltViewModel
import rs.readahead.washington.mobile.domain.entity.reports.ReportFormInstance
import rs.readahead.washington.mobile.domain.entity.reports.TellaReportServer
import rs.readahead.washington.mobile.domain.usecases.reports.GetReportsServersUseCase
import rs.readahead.washington.mobile.domain.usecases.reports.SaveReportFormInstanceUseCase
import javax.inject.Inject


@HiltViewModel
class ReportsEntryViewModel @Inject constructor(
    private val getReportsServersUseCase: GetReportsServersUseCase,
    private val saveReportFormInstanceUseCase: SaveReportFormInstanceUseCase
) :
    ViewModel() {

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> get() = _progress
    private val _serversList = MutableLiveData<List<TellaReportServer>>()
    val serversList: LiveData<List<TellaReportServer>> get() = _serversList
    private var _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error
    private val _draftReportFormInstance = MutableLiveData<ReportFormInstance>()
    val draftReportFormInstance: LiveData<ReportFormInstance> get() = _draftReportFormInstance

    init {
        listServers()
    }

    private fun listServers() {
        _progress.postValue(true)
        getReportsServersUseCase.execute(
            onSuccess = { result ->
                _serversList.postValue(result)
            },
            onError = {
                _error.postValue(it)
            },
            onFinished = {
                _progress.postValue(false)
            }
        )
    }

    fun saveDraft(reportFormInstance: ReportFormInstance) {
        _progress.postValue(true)
        saveReportFormInstanceUseCase.execute(
            onSuccess = { result ->
                _draftReportFormInstance.postValue(result)
            },
            onError = {
                _error.postValue(it)
            },
            onFinished = {
                _progress.postValue(false)
            }
        )
    }

    fun getDraftFormInstance(
        title: String,
        description: String,
        files: List<VaultFile>?,
        server: TellaReportServer
    ): ReportFormInstance {
        return ReportFormInstance(
            title = title,
            description = description,

        )
    }
}

