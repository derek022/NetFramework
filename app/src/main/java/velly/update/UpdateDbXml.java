package velly.update;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: admin
 * @Date: 2019/10/10
 * @Describe : 升级更新数据库
 */
public class UpdateDbXml {
    /**
     * 升级脚本列表
     */
    private List<UpdateStep> updateSteps;
    /**
     * 升级版本
     */
    private List<CreateVersion> createVersions;

    public UpdateDbXml(Document document) {
        {
            // 获取升级脚本
            NodeList updateSteps = document.getElementsByTagName("updateStep");
            this.updateSteps = new ArrayList<>();
            for (int i = 0; i < updateSteps.getLength(); i++) {
                Element ele = (Element) (updateSteps.item(i));
                UpdateStep step = new UpdateStep(ele);
                this.updateSteps.add(step);
            }
        }
        {
            /**
             * 获取各升级脚本
             */
            NodeList createVersions = document.getElementsByTagName("createVersion");
            this.createVersions = new ArrayList<>();
            for (int i = 0; i < createVersions.getLength(); i++) {
                Element ele = (Element) createVersions.item(i);
                CreateVersion cv = new CreateVersion(ele);
                this.createVersions.add(cv);
            }
        }
    }

    /**
     * 获取各个升级版本
     *
     * @return
     */
    public List<CreateVersion> getCreateVersions() {
        return createVersions;
    }

    /**
     * 获取各个升级步骤
     *
     * @return
     */
    public List<UpdateStep> getUpdateSteps() {
        return updateSteps;
    }


}
