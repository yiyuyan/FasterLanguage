package cn.ksmcbrigade.fl.mixin;

import cn.ksmcbrigade.fl.FasterLanguage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow @Final private LanguageManager languageManager;

    @Shadow public abstract ResourceManager getResourceManager();

    @Inject(method = "reloadResourcePacks()Ljava/util/concurrent/CompletableFuture;",at = @At("HEAD"),cancellable = true)
    public void reloadRes(CallbackInfoReturnable<CompletableFuture<Void>> cir){
        if(FasterLanguage.langReload){
            this.languageManager.onResourceManagerReload(this.getResourceManager());
            FasterLanguage.langReload = false;
            cir.setReturnValue(null);
            cir.cancel();
        }
    }
}
