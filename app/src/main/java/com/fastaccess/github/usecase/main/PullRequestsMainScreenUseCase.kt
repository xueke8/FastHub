package com.fastaccess.github.usecase.main

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.rx2.Rx2Apollo
import com.fastaccess.data.persistence.models.MainIssuesPullsModel
import com.fastaccess.data.repository.LoginRepositoryProvider
import com.fastaccess.data.repository.MainIssuesPullsRepositoryProvider
import com.fastaccess.domain.usecase.base.BaseObservableUseCase
import github.GetPullRequestsQuery
import github.type.IssueState
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Kosh on 16.06.18.
 */
class PullRequestsMainScreenUseCase @Inject constructor(
        private val loginRepository: LoginRepositoryProvider,
        private val mainIssues: MainIssuesPullsRepositoryProvider,
        private val apolloClient: ApolloClient
) : BaseObservableUseCase() {
    var state: IssueState = IssueState.OPEN

    override fun buildObservable(): Observable<*> = loginRepository.getLogin()
            .flatMapObservable { loginModel ->
                return@flatMapObservable loginModel.login?.let { login ->
                    Rx2Apollo.from(apolloClient.query(GetPullRequestsQuery.builder()
                            .login(login)
                            .build()))
                            .map { it.data()?.user?.pullRequests?.nodes }
                            .map { value ->
                                mainIssues.deleteAllPrs()
                                mainIssues.insert(value.asSequence().map {
                                    MainIssuesPullsModel(it.id, it.databaseId, it.number, it.title,
                                            it.repository.nameWithOwner, it.comments.totalCount, it.state.name)
                                }.toList())
                            }
                } ?: Observable.empty()
            }
}