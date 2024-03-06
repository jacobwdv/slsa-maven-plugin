package io.slsa.maven.plugin.utils.git;

import java.io.IOException;

import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import io.intoto.attestation.DigestSet;
import io.intoto.attestation.custom.resource.descriptors.git.GitRepositoryResourceDescriptor;
import io.intoto.attestation.utils.exceptions.GitRepoUrlException;
import io.slsa.maven.plugin.exceptions.GitRepositoryException;

public class GitUtils {

    public GitRepositoryResourceDescriptor getGitRepositoryResourceDescriptor() throws GitRepositoryException {
        try {
            Repository repository = getLocalRepository();
            return buildGitRepositoryResourceDescriptor(repository);
        } catch (Exception e) {
            throw new GitRepositoryException(e.getMessage(), e);
        }
    }

    private Repository getLocalRepository() throws IOException {
        FileRepositoryBuilder repoBuilder = new FileRepositoryBuilder();
        return repoBuilder.findGitDir().readEnvironment().build();
    }

    private GitRepositoryResourceDescriptor buildGitRepositoryResourceDescriptor(Repository repository) throws GitRepoUrlException, IOException {
        String gitRemoteOriginUrl = getRemoteOriginUrl(repository);
        String gitHubRepoUrl = io.intoto.attestation.utils.GitUtils.buildGitHubRepoUrl(gitRemoteOriginUrl);

        String ref = repository.getFullBranch();
        String commit = repository.resolve("HEAD").getName();
        DigestSet digest = new DigestSet();
        digest.put(DigestSet.GITCOMMIT, commit);

        GitRepositoryResourceDescriptor.Builder builder = new GitRepositoryResourceDescriptor.Builder(gitRemoteOriginUrl);
        builder.ref(ref)
                .digest(digest)
                .uri("git+" + gitHubRepoUrl + "@" + ref);
        return builder.build();
    }

    private String getRemoteOriginUrl(Repository repository) {
        return repository.getConfig().getString(ConfigConstants.CONFIG_REMOTE_SECTION, "origin", "url");
    }

}
