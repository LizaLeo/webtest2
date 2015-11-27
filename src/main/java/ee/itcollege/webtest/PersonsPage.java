package ee.itcollege.webtest;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ee.itcollege.webtest.entity.Person;
import ee.itcollege.webtest.service.PersonService;

public class PersonsPage extends WebPage {
	
	private static Logger LOG = Logger.getLogger(PersonsPage.class);
	 

    @SpringBean
    private PersonService personService;

    private IModel<List<Person>> personModel = new LoadableDetachableModel<List<Person>>() {
        @Override
        public List<Person> load() {
        	LOG.debug("reaload list");
            return personService.getPersons();
        }
    };
    private WebMarkupContainer listContainer = new WebMarkupContainer("list-container");
        
    public PersonsPage(final PageParameters parameters) {
        super(parameters);
        
        Form<Person> form = new Form<Person>("add-person-form",
                // CompoundPropertyModel asks for field "name" to automatically
                // update person.getName() person.setName()
                new CompoundPropertyModel<Person>(new Person())) {
        	@Override
        	protected void onSubmit() {
                // save person
                personService.save(getModelObject());

                // create new person object for form
                setDefaultModelObject(new Person());
        	}
        };
        add(form);
        
        final TextField<String> nameField = new TextField<String>("name");
        
        // this is for adding javascript to component
        nameField.add(new Behavior() {
            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                // wicket by default comes with jQuery
                response.render(new OnDomReadyHeaderItem(String.format("$('#%s').focus()", component.getMarkupId())));
            }
        });
        nameField.setOutputMarkupId(true);
        form.add(nameField);
        
        Button submit = new Button("add-person");
        submit.add(new AjaxFormSubmitBehavior(form, "click") {
            @Override
            public void onSubmit(AjaxRequestTarget target) {
                // update list and field
                target.add(listContainer, nameField);
            }
        });
        form.add(submit);


        listContainer.add(new ListView<Person>("person", personModel) {
            @Override
            protected void populateItem(ListItem<Person> item) {
                Person person = item.getModelObject();
                item.add(new Label("name", person.getName()));
                item.add(new AjaxLink<Person>("delete", item.getModel()) {
					@Override
					public void onClick(AjaxRequestTarget target) {
						Person p = getModelObject();
						personService.delete(p);
						personModel.detach();
						target.add(listContainer);
					}
				});
            }
        });
        listContainer.setOutputMarkupId(true);
        add(listContainer);

    }
    
    @Override
    public void detachModels() {
    	super.detachModels();
    	personModel.detach();
    }

}
