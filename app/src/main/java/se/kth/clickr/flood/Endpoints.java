
package se.kth.clickr.flood;
import org.immutables.value.Value;
import java.util.List;
import org.immutables.gson.Gson;

@Gson.TypeAdapters
public class Endpoints{
        @Value.Immutable public static abstract class UserLogin{public abstract String username();public abstract String passwordInput();}
    @Value.Immutable public static abstract class UserEmptyResult{}
    @Value.Immutable public static abstract class UserGetAccountParams{}
    @Value.Immutable public static abstract class UserProfile{public abstract String id();public abstract String fullName();public abstract String username();public abstract String email();public abstract String avatar();public abstract String bio();public abstract long registered();public abstract long lastSeen();public abstract List<String> groups();}
    @Value.Immutable public static abstract class UserRegister{public abstract String fullName();public abstract String username();public abstract String email();public abstract String passwordInput();}
    @Value.Immutable public static abstract class UserNoResult{}
    @Value.Immutable public static abstract class UserSearchParams{public abstract String query();}
    @Value.Immutable public static abstract class UserPublic{public abstract String id();public abstract String fullName();public abstract String username();public abstract String avatar();public abstract String bio();}
    @Value.Immutable public static abstract class UserSearchResult{public abstract List<UserPublic> items();}
    @Value.Immutable public static abstract class UserRegisterGoogleParams{public abstract String username();public abstract String password();}
    @Value.Immutable public static abstract class UserGetProfileParams{public abstract String user();}
    @Value.Immutable public static abstract class UserAuthenticateGoogleParams{public abstract String token();}
    @Value.Immutable public static abstract class UserCheckUsernameParams{public abstract String username();}
    @Value.Immutable public static abstract class UserCheckUsernameResult{public abstract boolean isTaken();}
    @Value.Immutable public static abstract class TestScenariosCreate{public abstract String title();public abstract String description();public abstract boolean isPrivate();}
    @Value.Immutable public static abstract class TestScenariosIdContainer{public abstract String id();}
    @Value.Immutable public static abstract class TestScenariosTestScenario{public abstract String id();public abstract String ownerId();public abstract String title();public abstract String description();public abstract boolean isPrivate();public abstract List<String> sharedWith();public abstract List<String> invitedProbands();public abstract List<String> tracks();}
    @Value.Immutable public static abstract class TestScenariosNoParams{}
    @Value.Immutable public static abstract class TestScenariosResultEnvelope{public abstract List<TestScenariosTestScenario> items();}
    @Value.Immutable public static abstract class TestScenariosStreamSingleParams{public abstract String id();}
    @Value.Immutable public static abstract class TestScenariosStreamResult{public abstract String type();public abstract String id();public abstract TestScenariosTestScenario value();public abstract List<TestScenariosTestScenario> values();}
    @Value.Immutable public static abstract class TestScenariosUpdate{public abstract String title();public abstract String description();public abstract boolean isPrivate();public abstract List<String> sharedWith();public abstract List<String> invitedProbands();public abstract List<String> tracks();}
    @Value.Immutable public static abstract class TestScenariosUpdateParams{public abstract TestScenariosUpdate item();public abstract String id();}
    @Value.Immutable public static abstract class TestScenariosEmptyResult{}
    @Value.Immutable public static abstract class TestScenariosGetOpenScenariosParams{}
    @Value.Immutable public static abstract class TestScenariosGetOpenScenariosResult{public abstract List<TestScenariosTestScenario> openScenarios();}
    @Value.Immutable public static abstract class MainNoParams{}
    @Value.Immutable public static abstract class MainAboutResult{public abstract long time();}
    @Value.Immutable public static abstract class ScenarioSessionsGenerateSessionParams{public abstract String scenarioId();}
    @Value.Immutable public static abstract class ScenarioSessionsGenerateSessionResult{public abstract String id();}
    @Value.Immutable public static abstract class ScenarioSessionsListSessionsParams{}
    @Value.Immutable public static abstract class ScenarioSessionsResultTrack{public abstract String trackId();public abstract boolean played();public abstract long offset();}
    @Value.Immutable public static abstract class ScenarioSessionsScenarioSession{public abstract String id();public abstract String scenario();public abstract String title();public abstract String description();public abstract String user();public abstract long created();public abstract long lastUpdated();public abstract List<ScenarioSessionsResultTrack> tracks();}
    @Value.Immutable public static abstract class ScenarioSessionsListSessionsResult{public abstract List<ScenarioSessionsScenarioSession> items();}
    @Value.Immutable public static abstract class ScenarioSessionsStreamSingleParams{public abstract String id();}
    @Value.Immutable public static abstract class ScenarioSessionsSaveOffsetParams{public abstract String id();public abstract String trackId();public abstract long offset();}
    @Value.Immutable public static abstract class ScenarioSessionsNoResult{}
    @Value.Immutable public static abstract class ScenarioSessionsButtonPress{public abstract String trackId();public abstract String session();public abstract String key();public abstract long offset();}
    @Value.Immutable public static abstract class ScenarioSessionsEmptyResult{}
    @Value.Immutable public static abstract class TracksFile{public abstract String fieldname();public abstract String originalname();public abstract String encoding();public abstract String mimetype();public abstract String destination();public abstract String filename();public abstract String path();public abstract long size();}
    @Value.Immutable public static abstract class TracksCreate{public abstract String userId();public abstract String title();public abstract String description();public abstract TracksFile file();}
    @Value.Immutable public static abstract class TracksUploadResult{public abstract String newId();}
    @Value.Immutable public static abstract class TracksStreamSingleParams{public abstract String id();}
    @Value.Immutable public static abstract class TracksButton{public abstract String key();public abstract String label();public abstract long enable();public abstract long disable();public abstract boolean skipOnClick();}
    @Value.Immutable public static abstract class TracksTrack{public abstract String id();public abstract String title();public abstract String description();public abstract List<String> sharedWith();public abstract String ownerId();public abstract long created();public abstract List<TracksButton> buttons();}
    @Value.Immutable public static abstract class TracksIdContainer{public abstract String id();}
    @Value.Immutable public static abstract class TracksDb{public abstract String id();public abstract String title();public abstract String description();public abstract List<String> sharedWith();public abstract String ownerId();public abstract String filePath();public abstract long created();public abstract List<TracksButton> buttons();}
    @Value.Immutable public static abstract class TracksGetFileParams{public abstract TracksDb track();}
    @Value.Immutable public static abstract class TracksFilePath{public abstract String path();}
    @Value.Immutable public static abstract class TracksNoParams{}
    @Value.Immutable public static abstract class TracksStreamResult{public abstract String type();public abstract String id();public abstract TracksTrack value();public abstract List<TracksTrack> values();}
    @Value.Immutable public static abstract class TracksUpdate{public abstract String title();public abstract String description();public abstract List<String> sharedWith();public abstract List<TracksButton> buttons();}
    @Value.Immutable public static abstract class TracksUpdateParams{public abstract TracksUpdate item();public abstract String id();}
    @Value.Immutable public static abstract class TracksEmptyResult{}

    
    public abstract static class UserLoginCallback{
        public abstract void result(UserEmptyResult result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request UserLogin(UserLogin params,final UserLoginCallback callback){
        return new Request("user","login",params,new Request.RequestCallback(UserEmptyResult.class){
            @Override
            public void res(Object result){
                callback.result((UserEmptyResult) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class UserGetAccountCallback{
        public abstract void result(UserProfile result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request UserGetAccount(UserGetAccountParams params,final UserGetAccountCallback callback){
        return new Request("user","getAccount",params,new Request.RequestCallback(UserProfile.class){
            @Override
            public void res(Object result){
                callback.result((UserProfile) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class UserRegisterCallback{
        public abstract void result(UserNoResult result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request UserRegister(UserRegister params,final UserRegisterCallback callback){
        return new Request("user","register",params,new Request.RequestCallback(UserNoResult.class){
            @Override
            public void res(Object result){
                callback.result((UserNoResult) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class UserSearchCallback{
        public abstract void result(UserSearchResult result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request UserSearch(UserSearchParams params,final UserSearchCallback callback){
        return new Request("user","search",params,new Request.RequestCallback(UserSearchResult.class){
            @Override
            public void res(Object result){
                callback.result((UserSearchResult) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class UserRegisterGoogleCallback{
        public abstract void result(UserNoResult result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request UserRegisterGoogle(UserRegisterGoogleParams params,final UserRegisterGoogleCallback callback){
        return new Request("user","registerGoogle",params,new Request.RequestCallback(UserNoResult.class){
            @Override
            public void res(Object result){
                callback.result((UserNoResult) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class UserGetProfileCallback{
        public abstract void result(UserPublic result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request UserGetProfile(UserGetProfileParams params,final UserGetProfileCallback callback){
        return new Request("user","getProfile",params,new Request.RequestCallback(UserPublic.class){
            @Override
            public void res(Object result){
                callback.result((UserPublic) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class UserAuthenticateGoogleCallback{
        public abstract void result(UserNoResult result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request UserAuthenticateGoogle(UserAuthenticateGoogleParams params,final UserAuthenticateGoogleCallback callback){
        return new Request("user","authenticateGoogle",params,new Request.RequestCallback(UserNoResult.class){
            @Override
            public void res(Object result){
                callback.result((UserNoResult) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class UserCheckUsernameCallback{
        public abstract void result(UserCheckUsernameResult result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request UserCheckUsername(UserCheckUsernameParams params,final UserCheckUsernameCallback callback){
        return new Request("user","checkUsername",params,new Request.RequestCallback(UserCheckUsernameResult.class){
            @Override
            public void res(Object result){
                callback.result((UserCheckUsernameResult) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class TestScenariosCreateCallback{
        public abstract void result(TestScenariosIdContainer result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request TestScenariosCreate(TestScenariosCreate params,final TestScenariosCreateCallback callback){
        return new Request("testScenarios","create",params,new Request.RequestCallback(TestScenariosIdContainer.class){
            @Override
            public void res(Object result){
                callback.result((TestScenariosIdContainer) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class TestScenariosGetCallback{
        public abstract void result(TestScenariosTestScenario result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request TestScenariosGet(TestScenariosIdContainer params,final TestScenariosGetCallback callback){
        return new Request("testScenarios","get",params,new Request.RequestCallback(TestScenariosTestScenario.class){
            @Override
            public void res(Object result){
                callback.result((TestScenariosTestScenario) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class TestScenariosGetAlltestScenariosCallback{
        public abstract void result(TestScenariosResultEnvelope result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request TestScenariosGetAlltestScenarios(TestScenariosNoParams params,final TestScenariosGetAlltestScenariosCallback callback){
        return new Request("testScenarios","getAlltestScenarios",params,new Request.RequestCallback(TestScenariosResultEnvelope.class){
            @Override
            public void res(Object result){
                callback.result((TestScenariosResultEnvelope) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class TestScenariosStreamSingleCallback{
        public abstract void result(TestScenariosTestScenario result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request TestScenariosStreamSingle(TestScenariosStreamSingleParams params,final TestScenariosStreamSingleCallback callback){
        return new Request("testScenarios","streamSingle",params,new Request.RequestCallback(TestScenariosTestScenario.class){
            @Override
            public void res(Object result){
                callback.result((TestScenariosTestScenario) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class TestScenariosStreamAllCallback{
        public abstract void result(TestScenariosStreamResult result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request TestScenariosStreamAll(TestScenariosNoParams params,final TestScenariosStreamAllCallback callback){
        return new Request("testScenarios","streamAll",params,new Request.RequestCallback(TestScenariosStreamResult.class){
            @Override
            public void res(Object result){
                callback.result((TestScenariosStreamResult) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class TestScenariosUpdateCallback{
        public abstract void result(TestScenariosEmptyResult result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request TestScenariosUpdate(TestScenariosUpdateParams params,final TestScenariosUpdateCallback callback){
        return new Request("testScenarios","update",params,new Request.RequestCallback(TestScenariosEmptyResult.class){
            @Override
            public void res(Object result){
                callback.result((TestScenariosEmptyResult) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class TestScenariosGetOpenScenariosCallback{
        public abstract void result(TestScenariosGetOpenScenariosResult result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request TestScenariosGetOpenScenarios(TestScenariosGetOpenScenariosParams params,final TestScenariosGetOpenScenariosCallback callback){
        return new Request("testScenarios","getOpenScenarios",params,new Request.RequestCallback(TestScenariosGetOpenScenariosResult.class){
            @Override
            public void res(Object result){
                callback.result((TestScenariosGetOpenScenariosResult) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class TestScenariosDeleteCallback{
        public abstract void result(TestScenariosEmptyResult result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request TestScenariosDelete(TestScenariosIdContainer params,final TestScenariosDeleteCallback callback){
        return new Request("testScenarios","delete",params,new Request.RequestCallback(TestScenariosEmptyResult.class){
            @Override
            public void res(Object result){
                callback.result((TestScenariosEmptyResult) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class MainAboutCallback{
        public abstract void result(MainAboutResult result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request MainAbout(MainNoParams params,final MainAboutCallback callback){
        return new Request("main","about",params,new Request.RequestCallback(MainAboutResult.class){
            @Override
            public void res(Object result){
                callback.result((MainAboutResult) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class ScenarioSessionsGenerateSessionCallback{
        public abstract void result(ScenarioSessionsGenerateSessionResult result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request ScenarioSessionsGenerateSession(ScenarioSessionsGenerateSessionParams params,final ScenarioSessionsGenerateSessionCallback callback){
        return new Request("scenarioSessions","generateSession",params,new Request.RequestCallback(ScenarioSessionsGenerateSessionResult.class){
            @Override
            public void res(Object result){
                callback.result((ScenarioSessionsGenerateSessionResult) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class ScenarioSessionsListSessionsCallback{
        public abstract void result(ScenarioSessionsListSessionsResult result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request ScenarioSessionsListSessions(ScenarioSessionsListSessionsParams params,final ScenarioSessionsListSessionsCallback callback){
        return new Request("scenarioSessions","listSessions",params,new Request.RequestCallback(ScenarioSessionsListSessionsResult.class){
            @Override
            public void res(Object result){
                callback.result((ScenarioSessionsListSessionsResult) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class ScenarioSessionsStreamCallback{
        public abstract void result(ScenarioSessionsScenarioSession result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request ScenarioSessionsStream(ScenarioSessionsStreamSingleParams params,final ScenarioSessionsStreamCallback callback){
        return new Request("scenarioSessions","stream",params,new Request.RequestCallback(ScenarioSessionsScenarioSession.class){
            @Override
            public void res(Object result){
                callback.result((ScenarioSessionsScenarioSession) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class ScenarioSessionsSaveOffsetCallback{
        public abstract void result(ScenarioSessionsNoResult result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request ScenarioSessionsSaveOffset(ScenarioSessionsSaveOffsetParams params,final ScenarioSessionsSaveOffsetCallback callback){
        return new Request("scenarioSessions","saveOffset",params,new Request.RequestCallback(ScenarioSessionsNoResult.class){
            @Override
            public void res(Object result){
                callback.result((ScenarioSessionsNoResult) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class ScenarioSessionsStoreButtonPressCallback{
        public abstract void result(ScenarioSessionsEmptyResult result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request ScenarioSessionsStoreButtonPress(ScenarioSessionsButtonPress params,final ScenarioSessionsStoreButtonPressCallback callback){
        return new Request("scenarioSessions","storeButtonPress",params,new Request.RequestCallback(ScenarioSessionsEmptyResult.class){
            @Override
            public void res(Object result){
                callback.result((ScenarioSessionsEmptyResult) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class TracksCreateCallback{
        public abstract void result(TracksUploadResult result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request TracksCreate(TracksCreate params,final TracksCreateCallback callback){
        return new Request("tracks","create",params,new Request.RequestCallback(TracksUploadResult.class){
            @Override
            public void res(Object result){
                callback.result((TracksUploadResult) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class TracksStreamSingleCallback{
        public abstract void result(TracksTrack result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request TracksStreamSingle(TracksStreamSingleParams params,final TracksStreamSingleCallback callback){
        return new Request("tracks","streamSingle",params,new Request.RequestCallback(TracksTrack.class){
            @Override
            public void res(Object result){
                callback.result((TracksTrack) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class TracksGetCallback{
        public abstract void result(TracksTrack result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request TracksGet(TracksIdContainer params,final TracksGetCallback callback){
        return new Request("tracks","get",params,new Request.RequestCallback(TracksTrack.class){
            @Override
            public void res(Object result){
                callback.result((TracksTrack) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class TracksFileCallback{
        public abstract void result(TracksFilePath result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request TracksFile(TracksGetFileParams params,final TracksFileCallback callback){
        return new Request("tracks","file",params,new Request.RequestCallback(TracksFilePath.class){
            @Override
            public void res(Object result){
                callback.result((TracksFilePath) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class TracksStreamAllCallback{
        public abstract void result(TracksStreamResult result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request TracksStreamAll(TracksNoParams params,final TracksStreamAllCallback callback){
        return new Request("tracks","streamAll",params,new Request.RequestCallback(TracksStreamResult.class){
            @Override
            public void res(Object result){
                callback.result((TracksStreamResult) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
        


    public abstract static class TracksUpdateCallback{
        public abstract void result(TracksEmptyResult result);
        public abstract void err(String errorCode,String description);
    }
    
    public static Request TracksUpdate(TracksUpdateParams params,final TracksUpdateCallback callback){
        return new Request("tracks","update",params,new Request.RequestCallback(TracksEmptyResult.class){
            @Override
            public void res(Object result){
                callback.result((TracksEmptyResult) result);
            }
            @Override
            public void err(String ec,String desc){
                callback.err(ec,desc);
            }
        });
    }
         
}
