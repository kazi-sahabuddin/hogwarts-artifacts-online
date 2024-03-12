package edu.hogwartsartifactsonline.wizard;


import edu.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WizardService {
    private final WizardRepository wizardRepository;

    public WizardService(WizardRepository wizardRepository) {
        this.wizardRepository = wizardRepository;
    }

    public Wizard findById(Integer wizardId){

      return this.wizardRepository.findById(wizardId).orElseThrow(()->new ObjectNotFoundException("wizard", wizardId));
    }

    public List<Wizard> findAll(){
        return this.wizardRepository.findAll();
    }

    public Wizard save(Wizard newWizard){
        return  this.wizardRepository.save(newWizard);
    }

    public Wizard update(Integer wizardId, Wizard update){
        return this.wizardRepository.findById(wizardId)
                .map(oldWizard -> {
                    oldWizard.setName(update.getName());
                    return this.wizardRepository.save(oldWizard);
                })
                .orElseThrow(()-> new ObjectNotFoundException("wizard", wizardId));
    }

    public void delete(Integer wizardId){
        Wizard wizard = this.wizardRepository.findById(wizardId).orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
        wizard.removeAllArtifacts();
        this.wizardRepository.deleteById(wizardId);
    }
}